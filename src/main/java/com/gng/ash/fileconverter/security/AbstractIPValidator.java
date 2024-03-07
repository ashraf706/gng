package com.gng.ash.fileconverter.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.ash.fileconverter.model.GeoLocation;
import com.gng.ash.fileconverter.model.ValidationResult;

import java.net.http.HttpResponse;
import java.util.Set;

abstract class AbstractIPValidator {
    private final Set<String> blockedCountries = Set.of("China", "Spain", "USA");
    private final Set<String> blockedDataCentres = Set.of("AWS", "GCP", "Azure");

    protected ValidationResult validateResponse(HttpResponse<String> response) {
        ObjectMapper objectMapper = new ObjectMapper();
        GeoLocation geoLocation;
        try {
            geoLocation = objectMapper.readValue(response.body(), GeoLocation.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new ValidationResult(isRequestBlackListed(geoLocation), geoLocation.getQuery(),
                geoLocation.getCountryCode(), geoLocation.getIsp());
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
