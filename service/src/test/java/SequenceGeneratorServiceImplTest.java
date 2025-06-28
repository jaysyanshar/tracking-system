import com.teleport.tracking.system.entity.constant.enumeration.ResponseCode;
import com.teleport.tracking.system.entity.constant.test.SequenceGeneratorTestVariables;
import com.teleport.tracking.system.entity.exception.BusinessLogicException;
import com.teleport.tracking.system.repository.api.DbSequenceRepository;
import com.teleport.tracking.system.service.impl.SequenceGeneratorServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SequenceGeneratorServiceImplTest extends SequenceGeneratorTestVariables {

  @InjectMocks
  private SequenceGeneratorServiceImpl sequenceGeneratorService;

  @Mock
  private DbSequenceRepository dbSequenceRepository;

  @BeforeEach
  public void setup() {
    ReflectionTestUtils.setField(sequenceGeneratorService, "trackingNumberRetryCount", RETRY_COUNT);
    ReflectionTestUtils.setField(sequenceGeneratorService, "trackingNumberRetryDelayMs", RETRY_DELAY_MS);
  }

  @AfterEach
  public void tearDown() {
    verifyNoMoreInteractions(dbSequenceRepository);
  }

  @Test
  public void generateTrackingNumber_success() {
    when(dbSequenceRepository.getNextSequence(SEQUENCE_KEY)).thenReturn(Mono.just(NEXT_SEQUENCE_VALUE));

    StepVerifier.create(sequenceGeneratorService.generateTrackingNumber())
        .expectNext(EXPECTED_TRACKING_NUMBER)
        .verifyComplete();

    verify(dbSequenceRepository).getNextSequence(SEQUENCE_KEY);
  }

  @Test
  public void generateTrackingNumber_optimisticLockingException_thenRetryAndSucceed() {
    when(dbSequenceRepository.getNextSequence(SEQUENCE_KEY))
        .thenReturn(Mono.error(new OptimisticLockingFailureException(OPTIMISTIC_LOCK_ERROR_MESSAGE)))
        .thenReturn(Mono.just(NEXT_SEQUENCE_VALUE));

    StepVerifier.create(sequenceGeneratorService.generateTrackingNumber())
        .expectNext(EXPECTED_TRACKING_NUMBER)
        .verifyComplete();

    verify(dbSequenceRepository, times(2)).getNextSequence(SEQUENCE_KEY);
  }

    @Test
    public void generateTrackingNumber_dataAccessException_thenRetryAndSucceed() {
        when(dbSequenceRepository.getNextSequence(SEQUENCE_KEY))
                .thenReturn(Mono.error(new DataAccessResourceFailureException(DATA_ACCESS_ERROR_MESSAGE)))
                .thenReturn(Mono.just(NEXT_SEQUENCE_VALUE));

        StepVerifier.create(sequenceGeneratorService.generateTrackingNumber())
                .expectNext(EXPECTED_TRACKING_NUMBER)
                .verifyComplete();

        verify(dbSequenceRepository, times(2)).getNextSequence(SEQUENCE_KEY);
    }

    @Test
    public void generateTrackingNumber_optimisticLockingException_exceedRetries() {
        // Arrange
        OptimisticLockingFailureException exception = new OptimisticLockingFailureException(OPTIMISTIC_LOCK_ERROR_MESSAGE);

        when(dbSequenceRepository.getNextSequence(SEQUENCE_KEY))
                .thenReturn(Mono.error(exception))
                .thenReturn(Mono.error(exception))
                .thenReturn(Mono.error(exception))
                .thenReturn(Mono.error(exception)); // One more than retry count

        // Act & Assert
        StepVerifier.create(sequenceGeneratorService.generateTrackingNumber())
                .expectErrorMatches(error ->
                    error instanceof BusinessLogicException
                        && ((BusinessLogicException) error).getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR)
                        && ((BusinessLogicException) error).getCode().equals(ResponseCode.GENERATE_ID_ERROR)
                )
                .verify();

        verify(dbSequenceRepository, times(RETRY_COUNT + 1)).getNextSequence(SEQUENCE_KEY);
    }

    @Test
    public void generateTrackingNumber_nonRetryableException() {
        // Arrange
        RuntimeException nonRetryableException = new RuntimeException(GENERAL_ERROR_MESSAGE);
        when(dbSequenceRepository.getNextSequence(SEQUENCE_KEY)).thenReturn(Mono.error(nonRetryableException));

        // Act & Assert
        StepVerifier.create(sequenceGeneratorService.generateTrackingNumber())
                .expectErrorMatches(error ->
                    error instanceof BusinessLogicException
                        && ((BusinessLogicException) error).getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR)
                        && ((BusinessLogicException) error).getCode().equals(ResponseCode.GENERATE_ID_ERROR)
                )
                .verify();

        verify(dbSequenceRepository).getNextSequence(SEQUENCE_KEY);
    }

    @Test
    public void generateTrackingNumber_businessLogicException() {
        // Arrange
        BusinessLogicException businessException = new BusinessLogicException(
            HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.GENERATE_ID_ERROR);

        when(dbSequenceRepository.getNextSequence(SEQUENCE_KEY)).thenReturn(Mono.error(businessException));

        // Act & Assert
        StepVerifier.create(sequenceGeneratorService.generateTrackingNumber())
                .expectErrorMatches(error ->
                    error instanceof BusinessLogicException
                        && ((BusinessLogicException) error).getCode().equals(businessException.getCode())
                )
                .verify();

        verify(dbSequenceRepository).getNextSequence(SEQUENCE_KEY);
    }
}
