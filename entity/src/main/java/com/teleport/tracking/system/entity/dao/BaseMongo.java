package com.teleport.tracking.system.entity.dao;

import com.teleport.tracking.system.entity.constant.BaseMongoFields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseMongo implements Serializable {

  @Id
  @Field(value = BaseMongoFields.ID)
  private String id;

  @CreatedBy
  @Field(value = BaseMongoFields.CREATED_BY)
  private String createdBy;

  @LastModifiedBy
  @Field(value = BaseMongoFields.UPDATED_BY)
  private String updatedBy;

  @CreatedDate
  @Field(value = BaseMongoFields.CREATED_AT)
  private Instant createdAt;

  @LastModifiedDate
  @Field(value = BaseMongoFields.UPDATED_AT)
  private Instant updatedAt;

  @Indexed
  @Field(value = BaseMongoFields.IS_DELETED)
  private Boolean isDeleted = Boolean.FALSE; // Initialize with default value

  // Pre-persist hook to set default values if they're null
  public void prePersist() {
    if (isDeleted == null) {
      isDeleted = Boolean.FALSE;
    }
  }
}
