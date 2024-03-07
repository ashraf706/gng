package com.gng.ash.fileconverter.controller;

import com.gng.ash.fileconverter.model.ValidationResult;
import com.gng.ash.fileconverter.service.AuditLogService;
import com.gng.ash.fileconverter.service.FileConverterService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Date;

import static com.gng.ash.fileconverter.ConstantsAndUtils.*;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileUploaderController {
    private final FileConverterService fileConverterService;
    private final AuditLogService auditLogService;

    public FileUploaderController(FileConverterService fileConverterService, AuditLogService auditLogService) {
        this.fileConverterService = fileConverterService;
        this.auditLogService = auditLogService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> fileUploading(@RequestBody MultipartFile file, HttpServletRequest request) {
        ValidationResult validationResult = (ValidationResult) request.getAttribute(VALIDATION_RESULT);
        final Instant requestTimeStamp = (Instant) request.getAttribute(REQUEST_TIMESTAMP);

        try {
            String result = fileConverterService.convert(file);
            updateLog(request, validationResult, requestTimeStamp, HttpStatus.OK.value());

            return ResponseEntity.ok()
                    .contentLength(result.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header( CONTENT_DISPOSITION, "attachment; filename=" + FILE_NAME)
                    .body(result);
        } catch (Exception e) {
            updateLog(request, validationResult, requestTimeStamp, HttpStatus.INTERNAL_SERVER_ERROR.value());
            log.error("Unexpected error occurred while converting the file", e);

            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Unexpected error occurred while converting the file. Please contact support");
        }

    }

    private void updateLog(HttpServletRequest request, ValidationResult validationResult, Instant requestTimeStamp, int status) {
        auditLogService.saveAuditLog(validationResult, status, new Date(requestTimeStamp.toEpochMilli()),
                calculateElapsedTime(requestTimeStamp), request.getRequestURI());
    }
}
