package com.teleport.tracking.system.entity.constant;

public final class ApiPath {

  // API Versioning
  public static final String VERSION = "/v1";
  public static final String BASE_PATH = "/api" + VERSION;

  // Tracking Number API
  public static final String TRACKING_NUMBER = BASE_PATH + "/tracking-number";
  public static final String TRACKING_NUMBER_NEXT = "/next";
}
