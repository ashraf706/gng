package com.gng.ash.fileconverter.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.net.http.HttpClient;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());

        return http.build();
    }

    @Bean
    public IPValidator createIPValidator(@Value("${application.environment:dev}") String environment){
        if(environment.equals("dev")) {
            return new WireMockIPValidator(createHttpClient());
        }
        else {
            return new IPValidatorImpl();
        }
    }

    @Bean
    public HttpClient createHttpClient() {
        return HttpClient.newHttpClient();
    }

}
