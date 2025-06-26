package com.teleport.tracking.system.repository.api;

import reactor.core.publisher.Mono;

public interface DbSequenceRepositoryCustom {

  Mono<Long> getNextSequence(String key);
}
