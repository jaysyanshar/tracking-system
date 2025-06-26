package com.teleport.tracking.system.entity.dao;

import com.teleport.tracking.system.entity.constant.DbCollection;
import com.teleport.tracking.system.entity.constant.DbSequenceFields;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DbCollection.DB_SEQUENCE)
public class DbSequence extends BaseMongo {

  @Indexed(unique = true)
  @Field(value = DbSequenceFields.KEY)
  private String key;

  @Field(value = DbSequenceFields.SEQUENCE)
  private Long sequence;
}
