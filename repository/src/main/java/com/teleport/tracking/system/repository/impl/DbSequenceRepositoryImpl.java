package com.teleport.tracking.system.repository.impl;

import com.teleport.tracking.system.entity.constant.BaseMongoFields;
import com.teleport.tracking.system.entity.constant.DbSequenceFields;
import com.teleport.tracking.system.entity.dao.DbSequence;
import com.teleport.tracking.system.repository.api.DbSequenceRepositoryCustom;
import com.teleport.tracking.system.repository.config.MongoAuditingConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Slf4j
@Repository
public class DbSequenceRepositoryImpl implements DbSequenceRepositoryCustom {

  private final ReactiveMongoTemplate mongoTemplate;
  private final MongoAuditingConfig mongoAuditingConfig;

  public DbSequenceRepositoryImpl(ReactiveMongoTemplate mongoTemplate, MongoAuditingConfig mongoAuditingConfig) {
    this.mongoTemplate = mongoTemplate;
    this.mongoAuditingConfig = mongoAuditingConfig;
  }

  @Override
  public Mono<Long> getNextSequence(String key) {
    return Mono.defer(() -> {
      Query query = new Query()
          .addCriteria(Criteria.where(DbSequenceFields.KEY).is(key))
          .addCriteria(Criteria.where(BaseMongoFields.IS_DELETED).is(false));

      Update update = new Update()
          // Set audit fields only when inserting a new document
          .setOnInsert(BaseMongoFields.CREATED_AT, Instant.now())
          .setOnInsert(BaseMongoFields.CREATED_BY, mongoAuditingConfig.getAuditor())
          .setOnInsert(BaseMongoFields.IS_DELETED, false)
          // Always update the updatedAt and updatedBy fields on every modification
          .set(BaseMongoFields.UPDATED_AT, Instant.now())
          .set(BaseMongoFields.UPDATED_BY, mongoAuditingConfig.getAuditor());

      update.inc(DbSequenceFields.SEQUENCE, 1);

      FindAndModifyOptions options = FindAndModifyOptions.options()
          .returnNew(true)
          .upsert(true);

      return mongoTemplate.findAndModify(query, update, options, DbSequence.class)
          .map(DbSequence::getSequence)
          .doOnNext(sequence -> log.debug("Generated sequence {} for key: {}", sequence, key));
    });
  }
}
