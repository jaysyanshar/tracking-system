package com.teleport.tracking.system.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teleport.tracking.system.entity.constant.enumeration.ResponseCode;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.Date;

@Getter
public class BaseResponse<T> extends ResponseEntity<BaseResponse.ResponseBody<T>> {

  private BaseResponse(ResponseBody<T> data, HttpStatus status) {
    super(data, status);
  }

  public static <T> BaseResponse<T> success(T data) {
    ResponseBody<T> responseBody = ResponseBody.of(ResponseCode.SUCCESS, ResponseCode.SUCCESS.getMessage(), data);
    return new BaseResponse<>(responseBody, HttpStatus.OK);
  }

  public static <T> BaseResponse<T> error(HttpStatus status, ResponseCode code, String message) {
    ResponseBody<T> responseBody = ResponseBody.of(code, message, null);
    return new BaseResponse<>(responseBody, status);
  }

  @Data
  protected static class ResponseBody<T> implements Serializable {

    @JsonProperty("code")
    private ResponseCode code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;

    @JsonProperty("server_time")
    private Date serverTime;

    protected static <T> ResponseBody<T> of(ResponseCode code, String message, T data) {
      return new ResponseBody<>(code, message, data);
    }

    private ResponseBody(ResponseCode code, String message, T data) {
      this.code = code;
      this.message = message;
      this.data = data;
      this.serverTime = new Date();
    }
  }
}
