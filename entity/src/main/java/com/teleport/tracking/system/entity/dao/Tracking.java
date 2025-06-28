package com.teleport.tracking.system.entity.dao;

import com.teleport.tracking.system.entity.constant.DbCollection;
import com.teleport.tracking.system.entity.constant.TrackingFields;
import com.teleport.tracking.system.entity.constant.enumeration.TrackingStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = DbCollection.TRACKING)
public class Tracking extends BaseMongo {

  @Indexed(unique = true)
  @Field(value = TrackingFields.TRACKING_NUMBER)
  private String trackingNumber; // format: TRN<unique_number>

  @Pattern(regexp = "^[A-Z]{2}$", message = "Origin country ID must be in ISO 3166-1 alpha-2 format")
  @Field(value = TrackingFields.ORIGIN_COUNTRY_ID)
  private String originCountryId; // pattern: ^[A-Z]{2}$

  @Pattern(regexp = "^[A-Z]{2}$", message = "Destination country ID must be in ISO 3166-1 alpha-2 format")
  @Field(value = TrackingFields.DESTINATION_COUNTRY_ID)
  private String destinationCountryId; // pattern: ^[A-Z]{2}$

  @DecimalMin(value = "0.001", message = "Weight must be greater than 0.001")
  @Field(value = TrackingFields.WEIGHT)
  private Double weight;

  @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "Customer ID must be a valid UUID")
  @Field(value = TrackingFields.CUSTOMER_ID)
  private String customerId; // UUID

  @Pattern(regexp = "^[A-Za-z0-9\\s]{1,100}$", message = "Customer name must be alphanumeric and up to 100 characters long")
  @Field(value = TrackingFields.CUSTOMER_NAME)
  private String customerName; // pattern: ^[A-Za-z0-9\s]{1,100}$

  @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Customer slug must be in kebab-case format")
  @Field(value = TrackingFields.CUSTOMER_SLUG)
  private String customerSlug; // kebab-case

  @Field(value = TrackingFields.ORDER_CREATED_AT)
  private Instant orderCreatedAt; // Date when the order was created

  @Field(value = TrackingFields.STATUS)
  private TrackingStatus status;
}
