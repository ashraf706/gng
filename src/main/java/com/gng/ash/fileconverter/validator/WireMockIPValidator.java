package com.gng.ash.fileconverter.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gng.ash.fileconverter.model.ValidationResult;
import jakarta.servlet.ServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;

@Slf4j
class WireMockIPValidator extends AbstractIPValidator implements IPValidator {
    private static final String WIREMOCK_ENDPOINT = "http://localhost:8029/json/";
    private final HttpClient httpClient;

    public WireMockIPValidator(HttpClient httpClient, List<String> ipHeaders, Set<String> blockedCountries, Set<String> blockedIsps) {
        super(ipHeaders, blockedCountries, blockedIsps);
        this.httpClient = httpClient;
    }

    @Override
    public ValidationResult validate(ServletRequest request) throws JsonProcessingException {
        HttpResponse<String> response = sendRequest(getRequestIp(request));
        return validateResponse(response);
    }

    private HttpResponse<String> sendRequest(final String requestAddress) {
        try {
            HttpRequest request = createRequest(WIREMOCK_ENDPOINT, requestAddress);
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            log.error("Mock validator failed to send request. Is Wiremock running?");
            throw new RuntimeException("Error occurred while trying to send request", e);
        }
    }
}
