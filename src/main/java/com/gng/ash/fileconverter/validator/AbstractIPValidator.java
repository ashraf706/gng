package com.gng.ash.fileconverter.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.ash.fileconverter.exceptions.InvalidIPException;
import com.gng.ash.fileconverter.model.GeoLocation;
import com.gng.ash.fileconverter.model.ValidationResult;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.Set;

abstract class AbstractIPValidator {
    private final List<String> ipHeaders;
    private final Set<String> blockedCountries;
    private final Set<String> blockedDataCentres;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    AbstractIPValidator(List<String> ipHeaders, Set<String> blockedCountries, Set<String> blockedDataCentres) {
        this.ipHeaders = ipHeaders;
        this.blockedCountries = blockedCountries;
        this.blockedDataCentres = blockedDataCentres;
    }

    protected HttpRequest createRequest(String endPoint, String address) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(new URI(endPoint + address))
                .GET()
                .build();
    }

    protected ValidationResult validateResponse(HttpResponse<String> response) throws JsonProcessingException {
        validateRemoteResponse(response);
        GeoLocation geoLocation = objectMapper.readValue(response.body(), GeoLocation.class);


        return new ValidationResult(isRequestBlackListed(geoLocation), geoLocation.getQuery(),
                geoLocation.getCountryCode(), geoLocation.getIsp());
    }

    protected String getRequestIp(ServletRequest request) {
        for (String header : ipHeaders) {
            String ip = Optional.ofNullable(((HttpServletRequest) request).getHeader(header)).orElse("");
            if (!ip.isBlank()) {
                return ip;
            }
        }

        return request.getRemoteAddr();
    }

    private static void validateRemoteResponse(HttpResponse<String> response) throws JsonProcessingException {
        JsonNode responseNode = objectMapper.readTree(response.body());
        if (responseNode.get("status").textValue().equals("fail")) {
            throw new InvalidIPException("Invalid ip: " + responseNode.get("query").textValue() + ", reason: " + responseNode.get("message").textValue());
        }
    }

    private boolean isRequestBlackListed(GeoLocation geoLocation) {
        return isCountryBlackListed(geoLocation.getCountry()) || isDataCentreBlackListed(geoLocation.getIsp());
    }

    private boolean isCountryBlackListed(String country) {
        return blockedCountries.contains(country);
    }

    private boolean isDataCentreBlackListed(String dataCentre) {
        return blockedDataCentres.contains(dataCentre);
    }
}
