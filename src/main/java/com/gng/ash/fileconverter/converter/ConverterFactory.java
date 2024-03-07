package com.gng.ash.fileconverter.converter;

import com.gng.ash.fileconverter.service.ConverterType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;

@Component
public class ConverterFactory {
    private final EnumMap<ConverterType, Converter> converters = new EnumMap<>(ConverterType.class);

    public Converter getConverter(ConverterType type) {
        if(converters.containsKey(type)) {
            return converters.get(type);
        }

        switch (type) {
            case JSON -> {
                JsonConverter jsonConverter = new JsonConverter();
                converters.put(type, jsonConverter);
                return jsonConverter;
            }
            case YML -> throw new UnsupportedOperationException("Not yet implemented");
            default -> {
                return converters.get(ConverterType.JSON);
            }
        }
    }

}
