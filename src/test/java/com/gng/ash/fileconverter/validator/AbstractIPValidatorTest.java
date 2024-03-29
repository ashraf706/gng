package com.gng.ash.fileconverter.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gng.ash.fileconverter.model.ValidationResult;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
abstract class AbstractIPValidatorTest {

    @Mock
    HttpClient httpClient;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpResponse<Object> httpResponse;

    IPValidator ipValidator;

    @Test
    @DisplayName("Successful validation")
    void shouldValidate() throws JsonProcessingException {
        when(httpResponse.body()).thenReturn("""
                {"query":"24.48.0.1","status":"success","country":"Canada","countryCode":"CA","region":"QC","regionName":"Quebec","city":"Montreal","zip":"H1K","lat":45.6085,"lon":-73.5493,"timezone":"America/Toronto","isp":"Le Groupe Videotron Ltee","org":"Videotron Ltee","as":"AS5769 Videotron Ltee"}
                """);

        ValidationResult result = ipValidator.validate(request);

        assertFalse(result.isFail());
        assertEquals("24.48.0.1", result.getIpAddress());
        assertEquals("CA", result.getCountryCode());
        assertEquals("Le Groupe Videotron Ltee", result.getIpProvider());
    }

    @Test
    @DisplayName("Validation failed due to black listed county")
    void fail_blackListed_countries() throws JsonProcessingException {
        when(httpResponse.body()).thenReturn("""
                {"query":"24.48.0.1","status":"success","country":"China","countryCode":"CN","region":"QC","regionName":"Quebec","city":"Montreal","zip":"H1K","lat":45.6085,"lon":-73.5493,"timezone":"America/Toronto","isp":"Any ISP","org":"Videotron Ltee","as":"AS5769 Videotron Ltee"}
                """);
        ValidationResult result = ipValidator.validate(request);

        assertTrue(result.isFail());
        assertEquals("24.48.0.1", result.getIpAddress());
        assertEquals("CN", result.getCountryCode());
        assertEquals("Any ISP", result.getIpProvider());
    }

    @Test
    @DisplayName("Validation failed due to black listed isp")
    void fail_blackListed_isp() throws JsonProcessingException {
        when(httpResponse.body()).thenReturn("""
                {"query":"24.48.0.1","status":"success","country":"Spain","countryCode":"SP","region":"QC","regionName":"Quebec","city":"Montreal","zip":"H1K","lat":45.6085,"lon":-73.5493,"timezone":"America/Toronto","isp":"AWS","org":"Videotron Ltee","as":"AS5769 Videotron Ltee"}
                """);

        ValidationResult result = ipValidator.validate(request);

        assertTrue(result.isFail());
        assertEquals("24.48.0.1", result.getIpAddress());
        assertEquals("SP", result.getCountryCode());
        assertEquals("AWS", result.getIpProvider());
    }
}
