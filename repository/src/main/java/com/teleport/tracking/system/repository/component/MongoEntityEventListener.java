package com.teleport.tracking.system.repository.component;

import com.teleport.tracking.system.entity.dao.BaseMongo;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class MongoEntityEventListener extends AbstractMongoEventListener<BaseMongo> {

  @Override
  public void onBeforeConvert(BeforeConvertEvent<BaseMongo> event) {
    BaseMongo entity = event.getSource();
    entity.prePersist();
  }
}
