package com.gng.ash.fileconverter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JsonEntry {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Transport")
    private String transport;

    @JsonProperty("Top Speed")
    private double topSpeed;
}
