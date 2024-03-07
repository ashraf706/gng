package com.gng.ash.fileconverter.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileConverterService {
    String convert(MultipartFile file);
    String convert(MultipartFile file, ConverterType converterType);
}
