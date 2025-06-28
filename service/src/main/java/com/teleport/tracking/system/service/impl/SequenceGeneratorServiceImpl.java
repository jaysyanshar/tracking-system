package com.teleport.tracking.system.service.impl;

import com.teleport.tracking.system.entity.constant.DbSequenceKey;
import com.teleport.tracking.system.entity.constant.enumeration.ResponseCode;
import com.teleport.tracking.system.entity.exception.BusinessLogicException;
import com.teleport.tracking.system.repository.api.DbSequenceRepository;
import com.teleport.tracking.system.service.api.SequenceGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@Service
public class SequenceGeneratorServiceImpl implements SequenceGeneratorService {

  private final DbSequenceRepository dbSequenceRepository;

  @Value("${tracking.number.generate.retry.max-attempts:3}")
  private int trackingNumberRetryCount;

  @Value("${tracking.number.generate.retry.interval-ms:50}")
  private long trackingNumberRetryDelayMs;

  @Autowired
  public SequenceGeneratorServiceImpl(DbSequenceRepository dbSequenceRepository) {
    this.dbSequenceRepository = dbSequenceRepository;
  }

  @Override
  public Mono<String> generateTrackingNumber() {
    return Mono.defer(() -> dbSequenceRepository.getNextSequence(DbSequenceKey.TRACKING_NUMBER_KEY))
        .map(sequence -> String.format("%s%010d", DbSequenceKey.TRACKING_NUMBER_PREFIX, sequence))
        // Retry logic for concurrency conflicts
        .retryWhen(Retry.backoff(trackingNumberRetryCount, Duration.ofMillis(trackingNumberRetryDelayMs))
            .filter(e ->
                e instanceof OptimisticLockingFailureException || e instanceof DataAccessResourceFailureException)
            .doBeforeRetry(retrySignal ->
                log.warn("Retrying tracking number generation after concurrency conflict, attempt: {}",
                    retrySignal.totalRetries() + 1)))
        .onErrorResume(e -> {
          if (e instanceof BusinessLogicException) {
            return Mono.error(e);
          }
          log.error("Error generating tracking number after retries", e);
          return Mono.error(new BusinessLogicException(HttpStatus.INTERNAL_SERVER_ERROR,
              ResponseCode.GENERATE_ID_ERROR, e.getMessage()));
        });
  }
}
