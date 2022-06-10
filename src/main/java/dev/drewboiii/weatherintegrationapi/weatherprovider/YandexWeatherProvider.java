package dev.drewboiii.weatherintegrationapi.weatherprovider;

import dev.drewboiii.weatherintegrationapi.dto.response.WeatherForecastDto;
import dev.drewboiii.weatherintegrationapi.dto.response.WeatherNowDto;
import dev.drewboiii.weatherintegrationapi.dto.response.WeatherTodayDto;
import dev.drewboiii.weatherintegrationapi.dto.request.yandex.YandexWeatherApiResponseDto;
import dev.drewboiii.weatherintegrationapi.dto.request.yandex.YandexWeatherNow;
import dev.drewboiii.weatherintegrationapi.exception.WeatherException;
import dev.drewboiii.weatherintegrationapi.exception.WeatherNotFoundException;
import dev.drewboiii.weatherintegrationapi.model.Location;
import dev.drewboiii.weatherintegrationapi.model.SupportedLanguages;
import dev.drewboiii.weatherintegrationapi.model.WeatherLocations;
import dev.drewboiii.weatherintegrationapi.model.WeatherProviders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component("YANDEX")
public class YandexWeatherProvider implements WeatherProvider {

    private static final String YANDEX_WEATHER_API_URL = "https://api.weather.yandex.ru/v2/forecast";

    private static final String LATITUDE_PARAM = "lat";
    private static final String LONGITUDE_PARAM = "lon";
    private static final String LANGUAGE_PARAM = "lang";
    private static final String DAYS_IN_FORECAST_PARAM = "limit";
    private static final String HAS_HOURS_IN_FORECAST_PARAM = "hours";
    private static final String HAS_PRECIPITATION_IN_FORECAST_PARAM = "extra";

    private static final Map<String, String> AVAILABLE_LANGUAGES =
            Map.of(SupportedLanguages.RUSSIAN.name().toLowerCase(), "ru_RU", SupportedLanguages.ENGLISH.name().toLowerCase(), "en_US");

    private final RestTemplate restTemplate;

    public YandexWeatherProvider(@Qualifier("YandexRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherNowDto getCurrent(Location location) throws WeatherException {
        return getCurrent(location, AVAILABLE_LANGUAGES.get(SupportedLanguages.ENGLISH.name()));
    }

    @Override
    public WeatherNowDto getCurrent(Location location, String language) throws WeatherException {
        String uri = UriComponentsBuilder.fromUriString(YANDEX_WEATHER_API_URL)
                .queryParam(LATITUDE_PARAM, location.getLatitude())
                .queryParam(LONGITUDE_PARAM, location.getLongitude())
                .queryParam(LANGUAGE_PARAM, language)
                .queryParam(DAYS_IN_FORECAST_PARAM, "1") // 7 by default
                .queryParam(HAS_HOURS_IN_FORECAST_PARAM, "false") // true by default
//                .queryParam(HAS_PRECIPITATION_IN_FORECAST_PARAM, "false") // precipitation, false by default
                .buildAndExpand()
                .toUriString();

        YandexWeatherApiResponseDto yandexWeatherApiResponseDto;
        try {
            log.info("Send request to Yandex Weather for location {}", location);
            yandexWeatherApiResponseDto = restTemplate.getForObject(uri, YandexWeatherApiResponseDto.class);
        } catch (RestClientException ex) {
            log.error("Yandex Weather responded with an error {}", ex.getMessage());
            throw new WeatherException(ex);
        }
        YandexWeatherApiResponseDto responseDto = Optional.ofNullable(yandexWeatherApiResponseDto)
                .orElseThrow(() -> new WeatherNotFoundException("Weather for " + location.getLocality() + "wasn't found."));
        YandexWeatherNow yandexWeatherNow = responseDto.getYandexWeatherNow();

        return WeatherNowDto.builder()
                .provider(WeatherProviders.YANDEX)
                .country(location.getCountry())
                .region(location.getRegion())
                .location(location.getLocality())
                .condition(yandexWeatherNow.getCondition())
                .humidity(yandexWeatherNow.getHumidity())
                .temperature(yandexWeatherNow.getTemperature())
                .feelsLike(yandexWeatherNow.getFeelsLike())
                .windSpeed(yandexWeatherNow.getWindSpeed())
                .windDirection(yandexWeatherNow.getWindDirection())
                .build();
    }

    @Override
    public WeatherTodayDto getToday(Location location) throws WeatherException {
//        String uri = UriComponentsBuilder.fromUriString(YANDEX_WEATHER_API_URL)
//                .queryParam(LATITUDE_PARAM, location.getLatitude())
//                .queryParam(LONGITUDE_PARAM, location.getLongitude())
//                .queryParam(LANGUAGE_PARAM, WeatherProviderLanguage.ENGLISH.getYandexLang())
//                .queryParam(DAYS_IN_FORECAST_PARAM, "1")
////                .queryParam(HAS_HOURS_IN_FORECAST_PARAM, "true") // true by default
////                .queryParam(HAS_PRECIPITATION_IN_FORECAST_PARAM, "false") // precipitation, false by default
//                .buildAndExpand()
//                .toUriString();
//        YandexWeatherApiResponseDto responseDto = Optional.ofNullable(restTemplate.getForObject(uri, YandexWeatherApiResponseDto.class))
//                .orElseThrow(() -> new WeatherNotFoundException("Weather for " + location.getLocality() + "wasn't found."));
        return WeatherTodayDto.builder().build();
    }

    @Override
    public WeatherForecastDto getForecast(Location location, int days) throws WeatherException {
//        String uri = UriComponentsBuilder.fromUriString(YANDEX_WEATHER_API_URL)
//                .queryParam(LATITUDE_PARAM, location.getLatitude())
//                .queryParam(LONGITUDE_PARAM, location.getLongitude())
//                .queryParam(LANGUAGE_PARAM, WeatherProviderLanguage.ENGLISH.getYandexLang())
//                .queryParam(DAYS_IN_FORECAST_PARAM, String.valueOf(days)) // 1 min, 7 max
////                .queryParam(HAS_HOURS_IN_FORECAST_PARAM, "true") // true by default
////                .queryParam(HAS_PRECIPITATION_IN_FORECAST_PARAM, "false") // precipitation, false by default
//                .buildAndExpand()
//                .toUriString();
//        YandexWeatherApiResponseDto responseDto = Optional.ofNullable(restTemplate.getForObject(uri, YandexWeatherApiResponseDto.class))
//                .orElseThrow(() -> new WeatherNotFoundException("Weather for " + location.getLocality() + "wasn't found."));
        return WeatherForecastDto.builder().build();
    }

    @Override
    public Map<String, String> getAvailableLanguages() {
        return AVAILABLE_LANGUAGES;
    }

    @Override
    public Map<String, Location> getAvailableLocations() {
        return Map.of(
                WeatherLocations.MOSCOW.name().toLowerCase(), Location.builder()
                        .country("Russia")
                        .locality("Moscow")
                        .region("Moscow")
                        .latitude(55.833333)
                        .longitude(37.620393)
                        .build(),
                WeatherLocations.SAMARA.name().toLowerCase(), Location.builder()
                        .country("Russia")
                        .locality("Samara")
                        .region("Samara")
                        .latitude(53.195538)
                        .longitude(50.101783)
                        .build()

        );
    }
}
