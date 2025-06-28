package com.teleport.tracking.system.entity.constant.test;

import com.teleport.tracking.system.entity.constant.enumeration.TrackingStatus;
import com.teleport.tracking.system.entity.dao.Tracking;
import com.teleport.tracking.system.entity.dto.GetNextTrackingNumberRequest;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

public abstract class TrackingNumberTestVariables {

  protected static final String TIMEZONE = "+07:00";
  protected static final String ORIGIN_COUNTRY_ID = "US";
  protected static final String DESTINATION_COUNTRY_ID = "SG";
  protected static final Double WEIGHT = 10.499;
  protected static final OffsetDateTime ORDER_CREATED_AT = OffsetDateTime.parse("2025-06-01T10:00:00+07:00");
  protected static final String CUSTOMER_ID = UUID.randomUUID().toString();
  protected static final String CUSTOMER_NAME = "Test Customer";
  protected static final String CUSTOMER_SLUG = "test-customer";
  protected static final String TRACKING_NUMBER = "TN123456789";
  protected static final String TRACKING_CREATED_AT_STR = "2025-06-01T10:00:00+07:00";
  protected static final Instant TRACKING_CREATED_AT = Instant.parse(TRACKING_CREATED_AT_STR);
  protected static final String AUDITOR = "system";
  protected static final String TRACKING_ID = UUID.randomUUID().toString();

  protected static final GetNextTrackingNumberRequest TRACKING_NUMBER_REQUEST = GetNextTrackingNumberRequest.builder()
      .originCountryId(ORIGIN_COUNTRY_ID)
      .destinationCountryId(DESTINATION_COUNTRY_ID)
      .weight(WEIGHT)
      .createdAt(ORDER_CREATED_AT)
      .customerId(CUSTOMER_ID)
      .customerName(CUSTOMER_NAME)
      .customerSlug(CUSTOMER_SLUG)
      .build();

  protected static final Tracking CREATED_TRACKING = Tracking.builder()
      .trackingNumber(TRACKING_NUMBER)
      .originCountryId(ORIGIN_COUNTRY_ID)
      .destinationCountryId(DESTINATION_COUNTRY_ID)
      .weight(WEIGHT)
      .orderCreatedAt(ORDER_CREATED_AT.toInstant())
      .customerId(CUSTOMER_ID)
      .customerName(CUSTOMER_NAME)
      .customerSlug(CUSTOMER_SLUG)
      .status(TrackingStatus.CREATED)
      .build();

  protected static final Tracking SAVED_TRACKING = Tracking.builder()
      .id(TRACKING_ID)
      .trackingNumber(TRACKING_NUMBER)
      .originCountryId(ORIGIN_COUNTRY_ID)
      .destinationCountryId(DESTINATION_COUNTRY_ID)
      .weight(WEIGHT)
      .orderCreatedAt(ORDER_CREATED_AT.toInstant())
      .customerId(CUSTOMER_ID)
      .customerName(CUSTOMER_NAME)
      .customerSlug(CUSTOMER_SLUG)
      .status(TrackingStatus.CREATED)
      .createdAt(TRACKING_CREATED_AT)
      .updatedAt(TRACKING_CREATED_AT)
      .createdBy(AUDITOR)
      .updatedBy(AUDITOR)
      .isDeleted(false)
      .build();
}
