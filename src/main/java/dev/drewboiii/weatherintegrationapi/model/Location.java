package dev.drewboiii.weatherintegrationapi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {

    private String country;
    private String region;
    private String locality;
    private Double latitude;
    private Double longitude;

}
