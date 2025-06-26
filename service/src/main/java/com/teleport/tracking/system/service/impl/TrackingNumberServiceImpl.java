package com.teleport.tracking.system.service.impl;

import com.teleport.tracking.system.entity.constant.enumeration.ResponseCode;
import com.teleport.tracking.system.entity.constant.enumeration.TrackingStatus;
import com.teleport.tracking.system.entity.dao.Tracking;
import com.teleport.tracking.system.entity.dto.GetNextTrackingNumberRequest;
import com.teleport.tracking.system.entity.dto.GetNextTrackingNumberResponse;
import com.teleport.tracking.system.entity.exception.BusinessLogicException;
import com.teleport.tracking.system.repository.api.TrackingRepository;
import com.teleport.tracking.system.service.api.SequenceGeneratorService;
import com.teleport.tracking.system.service.api.TrackingNumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class TrackingNumberServiceImpl implements TrackingNumberService {

  private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";

  private final TrackingRepository trackingRepository;
  private final SequenceGeneratorService sequenceGeneratorService;

  @Value("${server.timezone:+08:00}")
  private String timezone;

  public TrackingNumberServiceImpl(TrackingRepository trackingRepository, SequenceGeneratorService sequenceGeneratorService) {
    this.trackingRepository = trackingRepository;
    this.sequenceGeneratorService = sequenceGeneratorService;
  }

  @Override
  public Mono<GetNextTrackingNumberResponse> getNextTrackingNumber(GetNextTrackingNumberRequest request) {
    return Mono.defer(sequenceGeneratorService::generateTrackingNumber)
        .map(trackingNumber -> Tracking.builder()
            .trackingNumber(trackingNumber)
            .originCountryId(request.getOriginCountryId())
            .destinationCountryId(request.getDestinationCountryId())
            .weight(request.getWeight())
            .orderCreatedAt(request.getCreatedAt().toInstant())
            .customerId(request.getCustomerId())
            .customerName(request.getCustomerName())
            .customerSlug(request.getCustomerSlug())
            .status(TrackingStatus.CREATED)
            .build())
        .flatMap(trackingRepository::save)
        .map(savedTracking -> {
          final String createdAt = savedTracking.getCreatedAt()
              .atOffset(ZoneOffset.of(timezone))
              .format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
          return GetNextTrackingNumberResponse.builder()
              .trackingNumber(savedTracking.getTrackingNumber())
              .createdAt(createdAt)
              .build();
        })
        .onErrorResume(e -> {
          if (e instanceof BusinessLogicException) {
            return Mono.error(e);
          }
          log.error("Error generating tracking number", e);
          return Mono.error(new BusinessLogicException(HttpStatus.INTERNAL_SERVER_ERROR,
              ResponseCode.CREATE_DATA_ERROR, e.getMessage()));
        });
  }
}
