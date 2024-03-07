package com.gng.ash.fileconverter.converter;

import org.springframework.web.multipart.MultipartFile;

public interface Converter {
    String convert(MultipartFile file);
}
