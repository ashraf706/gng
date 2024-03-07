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
class IPValidatorImpl extends AbstractIPValidator implements IPValidator {
    private final String validatorEndpoint;
    private final HttpClient httpClient;

    public IPValidatorImpl(String validatorEndpoint, HttpClient httpClient, List<String> ipHeaders, Set<String> blockedCountries, Set<String> blockedIsps) {
        super(ipHeaders, blockedCountries, blockedIsps);
        this.validatorEndpoint = validatorEndpoint;
        this.httpClient = httpClient;
    }

    @Override
    public ValidationResult validate(ServletRequest request) throws JsonProcessingException {
        HttpResponse<String> response = sendRequest(getRequestIp(request));
        return validateResponse(response);
    }

    private HttpResponse<String> sendRequest(final String requestAddress) {
        try {
            HttpRequest request = createRequest(validatorEndpoint, requestAddress);
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            log.error("validator failed to send request to: " + validatorEndpoint);
            throw new RuntimeException("Error occurred while trying to send request", e);
        }
    }
}
