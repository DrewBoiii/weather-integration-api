package dev.drewboiii.weatherintegrationapi.dto.yandex;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class YandexWeatherNow {

    @JsonAlias("temp")
    private Integer temperature;

    @JsonAlias("feels_like")
    private Integer feelsLike;

    private String condition;

    @JsonAlias("cloudness")
    private Integer cloudiness;

    @JsonAlias("is_thunder")
    private Boolean isThunder;

    @JsonAlias("wind_speed")
    private Integer windSpeed;

    @JsonAlias("wind_dir")
    private String windDirection;

    private Integer humidity;

    private String daytime;

    private String season;

}
