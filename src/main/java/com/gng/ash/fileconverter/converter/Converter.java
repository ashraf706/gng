package com.gng.ash.fileconverter.converter;


import org.apache.wink.json4j.JSONArray;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface Converter {
    JSONArray convert(MultipartFile file);
}
