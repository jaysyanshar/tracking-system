package com.teleport.tracking.system.entity.constant.enumeration;

import lombok.Getter;

@Getter
public enum ResponseCode {
  SUCCESS("SUCCESS", "Success"),
  HTTP_ERROR("HTTP_ERROR", "HTTP Error"),
  CLIENT_ERROR("CLIENT_ERROR", "Client Error"),
  SYSTEM_ERROR("SYSTEM_ERROR", "System Error"),
  GENERATE_ID_ERROR("GENERATE_ID_ERROR", "Generate ID Error"),
  CREATE_DATA_ERROR("CREATE_DATA_ERROR", "Create Data Error");

  private final String code;
  private final String message;

  ResponseCode(String code, String message) {
    this.code = code;
    this.message = message;
  }
}
