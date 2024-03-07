package com.gng.ash.fileconverter.security;

import com.gng.ash.fileconverter.exceptions.InvalidIPException;
import com.gng.ash.fileconverter.model.ValidationResult;
import com.gng.ash.fileconverter.service.AuditLogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import static com.gng.ash.fileconverter.Constants.VALIDATION_RESULT;
import static com.gng.ash.fileconverter.Utils.calculateElapsedTime;

@Component
@Slf4j
public class IPFilter extends GenericFilterBean {
    private static final String REQUEST_URI = "/api/file/upload";
    private final IPValidator ipValidator;
    private final AuditLogService auditLogService;

    public IPFilter(IPValidator ipValidator, AuditLogService auditLogService) {
        this.ipValidator = ipValidator;
        this.auditLogService = auditLogService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestURI = ((HttpServletRequest) request).getRequestURI();

        if(!REQUEST_URI.equals(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        Instant requestTimeStamp =  Instant.now();
        ValidationResult result = ipValidator.validate(request);
        try {
            if (result.isFail()) {
                throw new InvalidIPException("Invalid request.");
            }
            request.setAttribute(VALIDATION_RESULT, result);
            filterChain.doFilter(request, response);
        } catch (InvalidIPException exception) {
            int responseStatus = HttpStatus.FORBIDDEN.value();
            String errorMsg = String.format("Invalid request. Ip: %1$s, Country code: %2$s, Provider: %3$s.", result.getIpAddress(), result.getCountryCode(), result.getIpProvider());

            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setStatus(responseStatus);
            httpServletResponse.getWriter().write(errorMsg);

            auditLogService.saveAuditLog(result, responseStatus, new Date(requestTimeStamp.toEpochMilli()),
                    calculateElapsedTime(requestTimeStamp), requestURI);
            log.error(errorMsg);
        }
    }

}
