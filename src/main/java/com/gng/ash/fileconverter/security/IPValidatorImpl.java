package com.gng.ash.fileconverter.security;

import com.gng.ash.fileconverter.model.ValidationResult;
import jakarta.servlet.ServletRequest;

public class IPValidatorImpl extends  AbstractIPValidator implements IPValidator {
    @Override
    public ValidationResult validate(ServletRequest request) {
        throw new UnsupportedOperationException("This method need to be implemented for production");
    }
}
