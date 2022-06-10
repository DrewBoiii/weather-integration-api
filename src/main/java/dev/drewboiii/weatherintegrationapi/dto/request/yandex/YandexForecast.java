package dev.drewboiii.weatherintegrationapi.dto.request.yandex;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class YandexForecast {

    private String date;

    private Integer week;

    private String sunrise;

    private String sunset;

    @JsonAlias("moon_code")
    private Integer moonCode;

    @JsonAlias("moon_text")
    private String moonText;

    private List<Hours> hours;

    @Data
    public static class Hours {

        private String hour;

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

    }

}
