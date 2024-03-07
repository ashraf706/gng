package com.gng.ash.fileconverter.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.ash.fileconverter.FileConverterApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = FileConverterApplication.class)
@AutoConfigureMockMvc
public class FileUploaderControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired protected ObjectMapper mapper;

    MockMultipartFile file;

    @BeforeEach
    void setUp() {
        String input = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1\n" +
                "3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5";

        file = new MockMultipartFile("file","anyFile.txt",MediaType.TEXT_PLAIN_VALUE, input.getBytes());
    }

    @Test
    @DisplayName("Successful request, without header")
    public void happyPath_status200() throws Exception {
        mvc.perform(multipart("/api/file/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(jsonPath("$[0].Name").value("John Smith"));
    }

    @Test
    @DisplayName("Successful request, with X-Forwarded-For")
    public void header_status200() throws Exception {
        mvc.perform(multipart("/api/file/upload")
                        .file(file)
                        .header("X-Forwarded-For", "24.40.0.1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(jsonPath("$[0].Name").value("John Smith"));
    }

    @Test
    @DisplayName("Failed request, due to blocked country")
    public void header_blocked_country() throws Exception {
        mvc.perform(multipart("/api/file/upload")
                        .file(file)
                        .header("X-Forwarded-For", "40.1.0.21"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(content().string("Invalid request. Ip: 40.1.0.21, Country code: CN, Provider: Some ip in China."));
    }

    @Test
    @DisplayName("Failed request, due to blocked isp")
    public void header_blocked_isp() throws Exception {
        mvc.perform(multipart("/api/file/upload")
                        .file(file)
                        .header("X-Forwarded-For", "50.1.0.21"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(content().string("Invalid request. Ip: 50.1.0.21, Country code: SP, Provider: AWS."));
    }
}


