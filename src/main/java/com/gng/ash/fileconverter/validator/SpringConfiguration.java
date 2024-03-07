package com.gng.ash.fileconverter.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Set;

@Configuration
public class SpringConfiguration {
    @Bean
    public IPValidator createIPValidator(@Value("${application.environment:dev}") String environment,
                                         @Value("${application.validator-endpoint:http://ip-api.com/json/}") String validatorEndpoint,
                                         @Value("#{'${application.ip-header}'.split(',')}") List<String> ipHeaders,
                                         @Value("#{'${application.blocked-countries}'.split(',')}") Set<String> blockedCountries,
                                         @Value("#{'${application.blocked-isps}'.split(',')}") Set<String> blockedIsps) {
        if (environment.equals("dev")) {
            return new WireMockIPValidator(createHttpClient(), ipHeaders, blockedCountries, blockedIsps);
        } else {
            return new IPValidatorImpl(validatorEndpoint, createHttpClient(), ipHeaders, blockedCountries, blockedIsps);
        }
    }

    @Bean
    public HttpClient createHttpClient() {
        return HttpClient.newHttpClient();
    }

}
