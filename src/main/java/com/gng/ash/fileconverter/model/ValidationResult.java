package com.gng.ash.fileconverter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValidationResult {
    boolean fail;
    String ipAddress;
    String countryCode;
    String ipProvider;
}
