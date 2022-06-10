package dev.drewboiii.weatherintegrationapi.dto.request.yandex;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class YandexWeatherApiResponseDto {

    private Long now;

    @JsonAlias("now_dt")
    private String nowLocalDateTime;

    @JsonAlias("geo_object")
    private YandexGeoObject yandexGeoObject;

    @JsonAlias("fact")
    private YandexWeatherNow yandexWeatherNow;

    @JsonAlias("forecasts")
    private List<YandexForecast> yandexForecast;

}
