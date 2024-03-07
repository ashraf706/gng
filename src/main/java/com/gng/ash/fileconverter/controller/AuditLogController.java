package com.gng.ash.fileconverter.controller;

import com.gng.ash.fileconverter.model.AuditLog;
import com.gng.ash.fileconverter.service.AuditLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audit")
public class AuditLogController {
    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public Iterable<AuditLog> getLogs() {
        return auditLogService.findAll();
    }
}
