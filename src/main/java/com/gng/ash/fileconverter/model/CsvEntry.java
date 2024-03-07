package com.gng.ash.fileconverter.model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CsvEntry {
    @CsvBindByPosition(position = 0)
    String uuId;
    @CsvBindByPosition(position = 1)
    String Id;
    @CsvBindByPosition(position = 2)
    String name;
    @CsvBindByPosition(position = 3)
    String likes;
    @CsvBindByPosition(position = 4)
    String transport;
    @CsvBindByPosition(position = 5)
    double avgSpeed;
    @CsvBindByPosition(position = 6)
    double topSpeed;
}
