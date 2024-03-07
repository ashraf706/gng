package com.gng.ash.fileconverter.service;

import com.gng.ash.fileconverter.converter.Converter;
import com.gng.ash.fileconverter.converter.ConverterFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
final class FileConverterServiceImpl implements FileConverterService {

    private final ConverterFactory converterFactory;

    public FileConverterServiceImpl(ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    @Override
    public String convert(MultipartFile file) {
        return convert(file, ConverterType.JSON);
    }

    @Override
    public String convert(MultipartFile file, ConverterType converterType) {
        Converter converter = converterFactory.getConverter(converterType);
        return converter.convert(file);
    }
}
