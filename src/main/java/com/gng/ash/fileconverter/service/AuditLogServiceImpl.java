package com.gng.ash.fileconverter.service;

import com.gng.ash.fileconverter.model.AuditLog;
import com.gng.ash.fileconverter.model.ValidationResult;
import com.gng.ash.fileconverter.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuditLogServiceImpl implements AuditLogService{
    private final AuditLogRepository repository;

    public AuditLogServiceImpl(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public AuditLog saveAuditLog(ValidationResult result, int statusCode, Date requestTimeStamp, long elapsedTime, String requestURI) {
        AuditLog auditLog = new AuditLog();
        auditLog.setHttpResponseCode(statusCode);
        auditLog.setUri(requestURI);
        auditLog.setIp(result.getIpAddress());
        auditLog.setCountryCode(result.getCountryCode());
        auditLog.setIsp(result.getIpProvider());
        auditLog.setRequestTime(requestTimeStamp);
        auditLog.setElapsedTime(elapsedTime);
        return repository.save(auditLog);
    }

    @Override
    public Iterable<AuditLog> findAll() {
        return  repository.findAll();
    }
}
