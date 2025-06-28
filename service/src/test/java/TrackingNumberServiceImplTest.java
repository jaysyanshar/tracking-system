import com.teleport.tracking.system.entity.constant.enumeration.ResponseCode;
import com.teleport.tracking.system.entity.constant.test.TrackingNumberTestVariables;
import com.teleport.tracking.system.entity.dao.Tracking;
import com.teleport.tracking.system.entity.exception.BusinessLogicException;
import com.teleport.tracking.system.repository.api.TrackingRepository;
import com.teleport.tracking.system.service.api.SequenceGeneratorService;
import com.teleport.tracking.system.service.impl.TrackingNumberServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrackingNumberServiceImplTest extends TrackingNumberTestVariables {

  @InjectMocks
  private TrackingNumberServiceImpl trackingNumberService;

  @Mock
  private TrackingRepository trackingRepository;

  @Mock
  private SequenceGeneratorService sequenceGeneratorService;

  @BeforeEach
  public void setup() {
    ReflectionTestUtils.setField(trackingNumberService, "timezone", TIMEZONE);
  }

  @AfterEach
  public void tearDown() {
    verifyNoMoreInteractions(trackingRepository, sequenceGeneratorService);
  }

  @Test
  public void getNextTrackingNumber_success() {
    when(sequenceGeneratorService.generateTrackingNumber()).thenReturn(Mono.just(TRACKING_NUMBER));
    when(trackingRepository.save(CREATED_TRACKING)).thenReturn(Mono.just(SAVED_TRACKING));

    StepVerifier.create(trackingNumberService.getNextTrackingNumber(TRACKING_NUMBER_REQUEST))
        .expectNextMatches(response ->
            response.getTrackingNumber().equals(TRACKING_NUMBER)
                && response.getCreatedAt().equals(TRACKING_CREATED_AT_STR)
        )
        .verifyComplete();

    verify(sequenceGeneratorService).generateTrackingNumber();
    verify(trackingRepository).save(CREATED_TRACKING);
  }

  @Test
  public void getNextTrackingNumber_sequenceGeneratorError() {
    final RuntimeException sequenceException = new RuntimeException("Failed to generate sequence");
    when(sequenceGeneratorService.generateTrackingNumber()).thenReturn(Mono.error(sequenceException));

    StepVerifier.create(trackingNumberService.getNextTrackingNumber(TRACKING_NUMBER_REQUEST))
        .expectErrorMatches(error ->
            error instanceof BusinessLogicException
                && ((BusinessLogicException) error).getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR)
                && ((BusinessLogicException) error).getCode().equals(ResponseCode.CREATE_DATA_ERROR)
        )
        .verify();

    verify(sequenceGeneratorService).generateTrackingNumber();
    verifyNoInteractions(trackingRepository);
  }

  @Test
  public void getNextTrackingNumber_repositorySaveError() {
    final RuntimeException repositoryException = new RuntimeException("Failed to save tracking");

    when(sequenceGeneratorService.generateTrackingNumber()).thenReturn(Mono.just(TRACKING_NUMBER));
    when(trackingRepository.save(any(Tracking.class))).thenReturn(Mono.error(repositoryException));

    StepVerifier.create(trackingNumberService.getNextTrackingNumber(TRACKING_NUMBER_REQUEST))
        .expectErrorMatches(error ->
            error instanceof BusinessLogicException
                && ((BusinessLogicException) error).getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR)
                && ((BusinessLogicException) error).getCode().equals(ResponseCode.CREATE_DATA_ERROR)
        )
        .verify();

    verify(sequenceGeneratorService).generateTrackingNumber();
    verify(trackingRepository).save(any(Tracking.class));
  }

  @Test
  public void getNextTrackingNumber_businessLogicException() {
    final BusinessLogicException businessException =
        new BusinessLogicException(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.GENERATE_ID_ERROR);

    when(sequenceGeneratorService.generateTrackingNumber()).thenReturn(Mono.error(businessException));

    StepVerifier.create(trackingNumberService.getNextTrackingNumber(TRACKING_NUMBER_REQUEST))
        .expectErrorMatches(error ->
            error instanceof BusinessLogicException
                && error == businessException
                && ((BusinessLogicException) error).getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR)
                && ((BusinessLogicException) error).getCode().equals(ResponseCode.GENERATE_ID_ERROR)
        )
        .verify();

    verify(sequenceGeneratorService).generateTrackingNumber();
    verify(trackingRepository, never()).save(any(Tracking.class));
  }
}
