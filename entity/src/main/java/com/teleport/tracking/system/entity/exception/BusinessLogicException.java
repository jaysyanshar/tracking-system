package com.teleport.tracking.system.entity.exception;

import com.teleport.tracking.system.entity.constant.enumeration.ResponseCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessLogicException extends RuntimeException {

  private final HttpStatus status;
  private final ResponseCode code;
  private final String message;

  public BusinessLogicException(HttpStatus status, ResponseCode code) {
    this(status, code, code.getMessage());
  }

  public BusinessLogicException(HttpStatus status, ResponseCode code, String message) {
    super(message);
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
