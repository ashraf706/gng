package com.gng.ash.fileconverter.service;

import com.gng.ash.fileconverter.model.AuditLog;
import com.gng.ash.fileconverter.model.ValidationResult;
import com.gng.ash.fileconverter.repository.AuditLogRepository;

import java.util.Date;

public interface AuditLogService {
    AuditLog saveAuditLog(ValidationResult result, int statusCode, Date requestTimeStamp, long elapsedTime, String requestURI);
    Iterable<AuditLog> findAll();
}
