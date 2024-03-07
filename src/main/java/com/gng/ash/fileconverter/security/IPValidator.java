package com.gng.ash.fileconverter.security;

import com.gng.ash.fileconverter.model.ValidationResult;
import jakarta.servlet.ServletRequest;

public interface IPValidator {
    ValidationResult validate(ServletRequest request);
}
