package com.teleport.tracking.system.repository.api;

import com.teleport.tracking.system.entity.dao.DbSequence;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbSequenceRepository extends ReactiveMongoRepository<DbSequence, String>, DbSequenceRepositoryCustom {
}
