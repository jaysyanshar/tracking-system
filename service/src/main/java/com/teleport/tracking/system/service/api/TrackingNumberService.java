package com.teleport.tracking.system.service.api;

import com.teleport.tracking.system.entity.dto.GetNextTrackingNumberRequest;
import com.teleport.tracking.system.entity.dto.GetNextTrackingNumberResponse;
import reactor.core.publisher.Mono;

public interface TrackingNumberService {
  Mono<GetNextTrackingNumberResponse> getNextTrackingNumber(GetNextTrackingNumberRequest request);
}
