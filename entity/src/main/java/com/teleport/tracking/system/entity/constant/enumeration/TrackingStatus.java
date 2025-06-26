package com.teleport.tracking.system.entity.constant.enumeration;

import lombok.Getter;

@Getter
public enum TrackingStatus {
  CREATED("CREATED", "Tracking Created"),
  IN_PROGRESS("IN_PROGRESS", "Tracking In Progress"),
  COMPLETED("COMPLETED", "Tracking Completed");

  private final String value;
  private final String description;

  TrackingStatus(String value, String description) {
    this.value = value;
    this.description = description;
  }
}
