package com.teleport.tracking.system.entity.constant.test;

import com.teleport.tracking.system.entity.constant.DbSequenceKey;

/**
 * Test variables for sequence generator tests
 */
public abstract class SequenceGeneratorTestVariables {

    // Sequence information
    protected static final String SEQUENCE_KEY = DbSequenceKey.TRACKING_NUMBER_KEY;
    protected static final String TRACKING_NUMBER_PREFIX = DbSequenceKey.TRACKING_NUMBER_PREFIX;
    protected static final Long NEXT_SEQUENCE_VALUE = 1234567890L;
    protected static final String EXPECTED_TRACKING_NUMBER = TRACKING_NUMBER_PREFIX + "1234567890";

    // Configuration for retry mechanism
    protected static final int RETRY_COUNT = 3;
    protected static final long RETRY_DELAY_MS = 50;

    // Error messages for various exception scenarios
    protected static final String OPTIMISTIC_LOCK_ERROR_MESSAGE = "Optimistic locking failed during sequence generation";
    protected static final String DATA_ACCESS_ERROR_MESSAGE = "Database connection failed during sequence access";
    protected static final String GENERAL_ERROR_MESSAGE = "General sequence generation error occurred";
}
