package com.teleport.tracking.system.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetNextTrackingNumberRequest implements Serializable {

  private String originCountryId;
  private String destinationCountryId;
  private Double weight;
  private OffsetDateTime createdAt;
  private String customerId;
  private String customerName;
  private String customerSlug;
}
