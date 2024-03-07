package com.gng.ash.fileconverter.service;

import com.gng.ash.fileconverter.converter.Converter;
import org.apache.wink.json4j.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class FileConverterImpl implements FileConverter {

    private final Converter converter;

    public FileConverterImpl(Converter converter) {
        this.converter = converter;
    }

    @Override
    public JSONArray convert(MultipartFile file) {
        return converter.convert(file);
    }
}
