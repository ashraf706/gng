package com.gng.ash.fileconverter.service;

import com.gng.ash.fileconverter.model.AuditLog;
import com.gng.ash.fileconverter.model.ValidationResult;

import java.util.Date;
import java.util.Optional;

public interface AuditLogService {
    Optional<AuditLog> saveAuditLog(ValidationResult result, int statusCode, Date requestTimeStamp, long elapsedTime, String requestURI);
    Iterable<AuditLog> findAll();
}
