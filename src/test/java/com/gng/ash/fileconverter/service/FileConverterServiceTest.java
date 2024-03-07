package com.gng.ash.fileconverter.service;

import com.gng.ash.fileconverter.converter.Converter;
import com.gng.ash.fileconverter.converter.ConverterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileConverterServiceTest {
	@Mock
	private ConverterFactory converterFactory;

	@Mock
	private Converter converter;

	private FileConverterService fileConverterService;
	@BeforeEach
	void setUp() {
		fileConverterService = new FileConverterServiceImpl(converterFactory);
	}

	@Test
	void should_call_converter() {
		String input = "any input";
		MockMultipartFile file = new MockMultipartFile("anyFile.txt", input.getBytes());
		when(converterFactory.getConverter(any())).thenReturn(converter);
		when(converter.convert(file)).thenReturn("any string");

		String result = fileConverterService.convert(file);

		assertEquals("any string", result);
		verify(converter, times(1)).convert(file);
	}

}
