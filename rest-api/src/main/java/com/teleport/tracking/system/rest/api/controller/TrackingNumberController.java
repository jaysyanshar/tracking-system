package com.teleport.tracking.system.rest.api.controller;

import com.teleport.tracking.system.entity.constant.ApiPath;
import com.teleport.tracking.system.entity.dto.BaseResponse;
import com.teleport.tracking.system.entity.dto.GetNextTrackingNumberRequest;
import com.teleport.tracking.system.entity.dto.GetNextTrackingNumberResponse;
import com.teleport.tracking.system.service.api.TrackingNumberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@Slf4j
@Validated
@RestController
@RequestMapping(ApiPath.TRACKING_NUMBER)
@Tag(name = "Tracking Numbers", description = "APIs for generating and managing tracking numbers")
public class TrackingNumberController {

  private final TrackingNumberService trackingNumberService;

  public TrackingNumberController(TrackingNumberService trackingNumberService) {
    this.trackingNumberService = trackingNumberService;
  }

  @Operation(
      summary = "Generate next tracking number",
      description = "Generates a unique tracking number based on order details"
  )
  @GetMapping(value = ApiPath.TRACKING_NUMBER_NEXT, produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<BaseResponse<GetNextTrackingNumberResponse>> getNextTrackingNumber(
      @Valid
      @NotNull(message = "Origin country ID cannot be null")
      @Pattern(regexp = "^[A-Z]{2}$", message = "Origin country ID must be in ISO 3166-1 alpha-2 format")
      @RequestParam("origin_country_id") String originCountryId,

      @Valid
      @NotNull(message = "Destination country ID cannot be null")
      @Pattern(regexp = "^[A-Z]{2}$", message = "Destination country ID must be in ISO 3166-1 alpha-2 format")
      @RequestParam("destination_country_id") String destinationCountryId,

      @Valid
      @NotNull(message = "Weight cannot be null")
      @DecimalMin(value = "0.001", message = "Weight must be greater than 0")
      @RequestParam("weight") Double weight,

      @Valid
      @NotNull(message = "Created at cannot be null")
      @DateTimeFormat(iso = ISO.DATE_TIME)
      @RequestParam("created_at") OffsetDateTime createdAt,

      @Valid
      @NotNull(message = "Customer ID cannot be null")
      @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
          message = "Customer ID must be a valid UUID")
      @RequestParam("customer_id") String customerId,

      @Valid
      @NotNull(message = "Customer name cannot be null")
      @Pattern(regexp = "^[a-zA-Z0-9 ]{1,100}$",
          message = "Customer name must be alphanumeric and up to 100 characters long")
      @RequestParam("customer_name") String customerName,

      @Valid
      @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
          message = "Customer slug must be in slug-case/kebab-case format")
      @RequestParam("customer_slug") String customerSlug
  ) {
    return Mono.just(GetNextTrackingNumberRequest.builder()
            .originCountryId(originCountryId)
            .destinationCountryId(destinationCountryId)
            .weight(weight)
            .createdAt(createdAt)
            .customerId(customerId)
            .customerName(customerName)
            .customerSlug(customerSlug)
            .build())
        .flatMap(trackingNumberService::getNextTrackingNumber)
        .map(BaseResponse::success);
  }
}
