package dev.drewboiii.weatherintegrationapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Weather {

    private Integer temperature;
    private String type;
    private Boolean precipitation;

}
