package com.gng.ash.fileconverter.controller;

import com.gng.ash.fileconverter.model.ValidationResult;
import com.gng.ash.fileconverter.service.AuditLogService;
import com.gng.ash.fileconverter.service.FileConverterService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.Optional;

import static com.gng.ash.fileconverter.ConstantsAndUtils.REQUEST_TIMESTAMP;
import static com.gng.ash.fileconverter.ConstantsAndUtils.VALIDATION_RESULT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileUploaderControllerTest {
    @Mock
    FileConverterService fileConverterService;

    @Mock
    AuditLogService auditLogService;

    @Mock
    HttpServletRequest request;

    private FileUploaderController controller;
    MockMultipartFile file;

    @BeforeEach
    void setUp() {
        controller = new FileUploaderController(fileConverterService, auditLogService);
        String input = "any input";
        file = new MockMultipartFile("anyFile.txt", input.getBytes());

        when(request.getAttribute(VALIDATION_RESULT)).thenReturn(new ValidationResult(false, "","",""));
        when(request.getAttribute(REQUEST_TIMESTAMP)).thenReturn(Instant.now());
        when(auditLogService.saveAuditLog(any(),anyInt(),any(),anyLong(),anyString())).thenReturn(Optional.empty());
        when(request.getRequestURI()).thenReturn("");
    }

    @Test
    void happy_path() {
        when(fileConverterService.convert(file)).thenReturn("[{}]");

        ResponseEntity<String> response = controller.fileUploading(file, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("[{}]", response.getBody());
    }

    @Test
    void exceptional_path() {
        when(fileConverterService.convert(file)).thenThrow(new RuntimeException("exceptional message"));

        ResponseEntity<String> response = controller.fileUploading(file, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected error occurred while converting the file. Please contact support", response.getBody());
    }
}
