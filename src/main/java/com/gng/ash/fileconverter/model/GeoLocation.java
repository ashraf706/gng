package com.gng.ash.fileconverter.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeoLocation {
    String query;
    String status;
    String country;
    String countryCode;
    String region;
    String regionName;
    String city;
    String zip;
    double lat;
    double lon;
    String timezone;
    String isp;
    String org;
    String as;
}
