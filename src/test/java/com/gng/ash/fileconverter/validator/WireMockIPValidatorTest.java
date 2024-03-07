package com.gng.ash.fileconverter.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WireMockIPValidatorTest extends AbstractIPValidatorTest{
    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        when(httpClient.send(any(), any())).thenReturn(httpResponse);
        ipValidator = new WireMockIPValidator(httpClient, new ArrayList<>(),
                Set.of("China", "Spain", "USA"), Set.of("AWS", "GCP", "Azure"));
    }
}
