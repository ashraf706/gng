package com.gng.ash.fileconverter.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gng.ash.fileconverter.model.CsvEntry;
import com.gng.ash.fileconverter.model.JsonEntry;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

@Slf4j
@Component
final class JsonConverter implements Converter {
    private static final char DELIMITER = '|';
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convert(MultipartFile file) {
        log.debug("converting file: {}", file.getName());
        String result;
        try (
                InputStream stream = file.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
        ) {
            ArrayList<JsonEntry> jsonEntries = new ArrayList<>();
            CsvToBean<CsvEntry> csvReader = getCsvReader(reader);
            final Iterator<CsvEntry> iterator = csvReader.stream().iterator();
            while (iterator.hasNext()) {
                CsvEntry entry = iterator.next();
                JsonEntry jsonEntry = new JsonEntry(entry.getName(), entry.getTransport(), entry.getTopSpeed());
                jsonEntries.add(jsonEntry);
            }

            result = mapper.writeValueAsString(jsonEntries);
            csvReader.getCapturedExceptions().forEach(ex -> log.error("Line {}: {}", ex.getLineNumber(), ex.getMessage()));

        } catch (IOException e) {
            throw new RuntimeException("Error occurred while parsing input file to JSON", e);
        }
        return result;
    }

    private static CsvToBean<CsvEntry> getCsvReader(BufferedReader reader) {
        return new CsvToBeanBuilder<CsvEntry>(reader)
                .withType(CsvEntry.class)
                .withSeparator(DELIMITER)
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .withVerifier(new CsvEntryVerifier())
                .withThrowExceptions(false)
                .build();
    }
}


