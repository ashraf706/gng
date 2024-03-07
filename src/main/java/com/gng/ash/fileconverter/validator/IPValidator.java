package com.gng.ash.fileconverter.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gng.ash.fileconverter.model.ValidationResult;
import jakarta.servlet.ServletRequest;

public interface IPValidator {
    ValidationResult validate(ServletRequest request) throws JsonProcessingException;
}
