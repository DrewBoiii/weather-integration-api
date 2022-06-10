package dev.drewboiii.weatherintegrationapi.dto.response;

import dev.drewboiii.weatherintegrationapi.model.WeatherProviders;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherNowDto {

    private WeatherProviders provider;
    private String location;
    private String region;
    private String country;
    private Integer temperature;
    private Integer feelsLike;
    private String condition;
    private Integer cloudiness;
    private Integer windSpeed;
    private String windDirection;
    private Integer humidity;
    private String daytime;
    private String season;

}
