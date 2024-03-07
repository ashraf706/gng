package com.gng.ash.fileconverter.repository;

import com.gng.ash.fileconverter.model.AuditLog;
import org.springframework.data.repository.CrudRepository;

public interface AuditLogRepository extends CrudRepository<AuditLog, String> {
}
