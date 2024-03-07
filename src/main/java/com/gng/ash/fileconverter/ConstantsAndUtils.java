package com.gng.ash.fileconverter;

import java.time.Instant;

public class ConstantsAndUtils {
    private ConstantsAndUtils() {}

    public static final String VALIDATION_RESULT = "validationResult";
    public static final String REQUEST_TIMESTAMP = "requestTimeStamp";
    public static final String FILE_NAME = "OutcomeFile.json";

    public static long calculateElapsedTime(Instant requestTimeStamp) {
        return Instant.now().toEpochMilli() - requestTimeStamp.toEpochMilli();
    }
}
