package com.gng.ash.fileconverter.validator;

import com.gng.ash.fileconverter.model.ValidationResult;
import com.gng.ash.fileconverter.service.AuditLogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IPFilterTest {
    @Mock
    IPValidator ipValidator;

    @Mock
    AuditLogService auditLogService;

    @Mock
    HttpServletRequest servletRequest;

    @Mock
    HttpServletResponse servletResponse;

    @Mock
    FilterChain filterChain;

    @Mock
    PrintWriter printWriter;

    private IPFilter ipFilter;

    @BeforeEach
    void setUp() {
        ipFilter = new IPFilter(ipValidator, auditLogService);
    }

    @Test
    @DisplayName("Successful validation")
    void should_validate() throws ServletException, IOException {
        ValidationResult result = new ValidationResult(false, "any-ip-address","any-country-code","any-ip-provider");
        when((servletRequest).getRequestURI()).thenReturn("/api/file/upload");
        when(ipValidator.validate(any())).thenReturn(result);

        ipFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(ipValidator, times(1)).validate(servletRequest);
        verify(filterChain, times(1)).doFilter(servletRequest, servletResponse);
    }

    @Test
    @DisplayName("Validation is not called")
    void shouldNot_validate() throws ServletException, IOException {
        when((servletRequest).getRequestURI()).thenReturn("/api/file");

        ipFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(ipValidator, times(0)).validate(servletRequest);
        verify(filterChain, times(1)).doFilter(servletRequest, servletResponse);
    }

    @Test
    @DisplayName("Handle validation exception")
    void should_throw_and_handle_exception() throws IOException, ServletException {
        String expectedMessage = "Invalid request. Ip: any-ip-address, Country code: any-country-code, Provider: any-ip-provider.";
        ValidationResult result = new ValidationResult(true, "any-ip-address","any-country-code","any-ip-provider");
        when((servletRequest).getRequestURI()).thenReturn("/api/file/upload");
        when(ipValidator.validate(any())).thenReturn(result);
        when(servletResponse.getWriter()).thenReturn(printWriter);

        ipFilter.doFilter(servletRequest, servletResponse, filterChain);

        verify(printWriter, times(1)).write(expectedMessage);
        verify(auditLogService, times(1)).saveAuditLog(any(), anyInt(), any(), anyLong(), any());
    }

}
