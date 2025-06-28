package com.teleport.tracking.system.entity.constant.enumeration;

import lombok.Getter;

@Getter
public enum TrackingStatus {
  CREATED("CREATED", "Tracking Created"),
  IN_TRANSIT("IN_TRANSIT", "Tracking In Transit"),
  DELIVERED("DELIVERED", "Tracking Delivered"),
  COMPLETED("COMPLETED", "Tracking Completed"),
  LOST("LOST", "Tracking Lost"),
  RETURNED("RETURNED", "Tracking Returned");

  private final String value;
  private final String description;

  TrackingStatus(String value, String description) {
    this.value = value;
    this.description = description;
  }
}
