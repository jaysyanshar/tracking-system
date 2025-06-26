package com.teleport.tracking.system.rest.api.controller;

import com.teleport.tracking.system.entity.constant.enumeration.ResponseCode;
import com.teleport.tracking.system.entity.dto.BaseResponse;
import com.teleport.tracking.system.entity.exception.BusinessLogicException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessLogicException.class)
  public Mono<BaseResponse<?>> handleBusinessLogicException(BusinessLogicException e) {
    log.warn("BusinessLogicException: {}", e.getMessage());
    return Mono.just(BaseResponse.error(e.getStatus(), e.getCode(), e.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<BaseResponse<?>> handleIllegalArgumentException(IllegalArgumentException e) {
    log.warn("IllegalArgumentException: {}", e.getMessage());
    return Mono.just(BaseResponse.error(
        HttpStatus.BAD_REQUEST, ResponseCode.CLIENT_ERROR, e.getMessage()));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<BaseResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
    log.warn("ConstraintViolationException: {}", e.getMessage());
    return Mono.just(BaseResponse.error(
        HttpStatus.BAD_REQUEST, ResponseCode.CLIENT_ERROR, e.getMessage()));
  }

  @ExceptionHandler(ErrorResponseException.class)
  public Mono<BaseResponse<?>> handleErrorResponseException(ErrorResponseException e) {
    log.warn("ErrorResponseException: {}", e.getMessage());
    return Mono.just(BaseResponse.error(
        HttpStatus.valueOf(e.getStatusCode().value()), ResponseCode.HTTP_ERROR, e.getMessage()));
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Mono<BaseResponse<?>> handleRuntimeException(RuntimeException e) {
    log.error("RuntimeException: {}", e.getMessage(), e);
    return Mono.just(BaseResponse.error(
        HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.SYSTEM_ERROR, e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Mono<BaseResponse<?>> handleGenericException(Exception e) {
    log.error("Exception: {}", e.getMessage(), e);
    return Mono.just(BaseResponse.error(
        HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.SYSTEM_ERROR, e.getMessage()));
  }
}
