package com.gng.ash.fileconverter.service;

import org.apache.wink.json4j.JSONArray;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileConverter {
    JSONArray convert(MultipartFile file);
}
