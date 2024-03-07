package com.gng.ash.fileconverter.converter;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.OrderedJSONObject;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.LinkedHashMap;

@Component
public class JsonConverter implements Converter {
    private static final String DELIMETER = "\\|";

    @Override
    public JSONArray convert(MultipartFile file) {
        System.out.println("Converting");
        JSONArray jsonArray = new JSONArray();
        try (
                InputStream stream = file.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                PrintWriter out = new PrintWriter("OutcomeFile.json")) {
            for (String line; (line = reader.readLine()) != null; ) {
                System.out.println(line);
                String[] data = line.split(DELIMETER);


                OrderedJSONObject jsonObject = new OrderedJSONObject();
                jsonObject.put("Name", data[2]);
                jsonObject.put("Transport", data[4]);
                jsonObject.put("Top Speed", data[6]);

                jsonArray.put(jsonObject);
            }

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
        return jsonArray;
    }
}
