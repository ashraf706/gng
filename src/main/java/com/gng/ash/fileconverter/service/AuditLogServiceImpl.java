package com.gng.ash.fileconverter.service;

import com.gng.ash.fileconverter.model.AuditLog;
import com.gng.ash.fileconverter.model.ValidationResult;
import com.gng.ash.fileconverter.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
final class AuditLogServiceImpl implements AuditLogService{
    private final AuditLogRepository repository;

    public AuditLogServiceImpl(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<AuditLog> saveAuditLog(ValidationResult result, int statusCode, Date requestTimeStamp, long elapsedTime, String requestURI) {
        if(result == null) {
            return Optional.empty();
        }
        AuditLog auditLog = new AuditLog();
        auditLog.setHttpResponseCode(statusCode);
        auditLog.setUri(requestURI);
        auditLog.setIp(result.getIpAddress());
        auditLog.setCountryCode(result.getCountryCode());
        auditLog.setIsp(result.getIpProvider());
        auditLog.setRequestTime(requestTimeStamp);
        auditLog.setElapsedTime(elapsedTime);
        return Optional.of(repository.save(auditLog));
    }

    @Override
    public Iterable<AuditLog> findAll() {
        return  repository.findAll();
    }
}
