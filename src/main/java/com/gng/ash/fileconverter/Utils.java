package com.gng.ash.fileconverter;

import java.time.Instant;

public interface Utils {
    static long calculateElapsedTime(Instant requestTimeStamp) {
        return Instant.now().toEpochMilli() - requestTimeStamp.toEpochMilli();
    }
}
