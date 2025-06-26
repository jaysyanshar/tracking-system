package com.teleport.tracking.system.service.api;

import reactor.core.publisher.Mono;

public interface SequenceGeneratorService {

  Mono<String> generateTrackingNumber();
}
