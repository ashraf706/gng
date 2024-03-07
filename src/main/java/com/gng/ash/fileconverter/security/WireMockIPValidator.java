package com.gng.ash.fileconverter.security;

import com.gng.ash.fileconverter.model.ValidationResult;
import jakarta.servlet.ServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class WireMockIPValidator extends AbstractIPValidator implements IPValidator {
    private static final String WIREMOCK_ENDPOINT = "http://localhost:8029/json/";
    private final HttpClient httpClient;

    public WireMockIPValidator(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public ValidationResult validate(ServletRequest request) {
        HttpResponse<String> response = sendRequest(request.getRemoteAddr());
        return validateResponse(response);
    }

    private HttpResponse<String> sendRequest(final String requestAddress) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(WIREMOCK_ENDPOINT + requestAddress))
                    .GET()
                    .build();

            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            log.error("Mock validator failed to send request. Is Wiremock running?");
            throw new RuntimeException("Error occurred while trying to send request", e);
        }
    }
}
