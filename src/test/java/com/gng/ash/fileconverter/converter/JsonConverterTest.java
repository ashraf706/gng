package com.gng.ash.fileconverter.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
class JsonConverterTest {
    private Converter jsonConverter;

    @BeforeEach
    void setUp() {
        jsonConverter = new JsonConverter();
    }

    @Test
    @DisplayName("Happy path")
    void pass_happyPath() {
        String input = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1\n" +
                "3ce2d17b-e66a-4c1e-bca3-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5";
        String expected = "[{\"Name\":\"John Smith\",\"Transport\":\"Rides A Bike\",\"Top Speed\":12.1},{\"Name\":\"Mike Smith\",\"Transport\":\"Drives an SUV\",\"Top Speed\":95.5}]";
        MockMultipartFile file = new MockMultipartFile("anyFile.txt", input.getBytes());

        String result = jsonConverter.convert(file);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Empty file")
    void pass_emptyFile() {
        String input = "";
        String expected = "[]";
        MockMultipartFile file = new MockMultipartFile("anyFile.txt", input.getBytes());

        String result = jsonConverter.convert(file);

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Invalid id or name etc")
    void pass_invalid_entries() {
        String input = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1\n" +
                "invalid-40eb1c9222c7|2X2D24|Mike Smith|Likes Grape|Drives an SUV|35.0|95.5";
        String expected = "[{\"Name\":\"John Smith\",\"Transport\":\"Rides A Bike\",\"Top Speed\":12.1}]";
        MockMultipartFile file = new MockMultipartFile("anyFile.txt", input.getBytes());

        String result = jsonConverter.convert(file);

        assertEquals(expected, result);
    }
}
