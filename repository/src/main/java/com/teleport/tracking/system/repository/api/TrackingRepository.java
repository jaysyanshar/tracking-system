package com.teleport.tracking.system.repository.api;

import com.teleport.tracking.system.entity.dao.Tracking;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingRepository extends ReactiveMongoRepository<Tracking, String> {
}
