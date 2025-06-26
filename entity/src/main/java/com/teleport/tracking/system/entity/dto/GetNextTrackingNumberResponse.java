package com.teleport.tracking.system.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetNextTrackingNumberResponse implements Serializable {

  @JsonProperty("tracking_number")
  private String trackingNumber;

  @JsonProperty("created_at")
  private String createdAt;
}

