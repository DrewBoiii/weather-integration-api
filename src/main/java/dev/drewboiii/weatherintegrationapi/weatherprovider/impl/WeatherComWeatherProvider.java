package dev.drewboiii.weatherintegrationapi.weatherprovider.impl;

import dev.drewboiii.weatherintegrationapi.dto.response.WeatherNowDto;
import dev.drewboiii.weatherintegrationapi.dto.request.weatherapicom.WeatherApiComResponseDto;
import dev.drewboiii.weatherintegrationapi.exception.WeatherException;
import dev.drewboiii.weatherintegrationapi.model.Location;
import dev.drewboiii.weatherintegrationapi.config.meta.SupportedLanguages;
import dev.drewboiii.weatherintegrationapi.config.meta.WeatherLocations;
import dev.drewboiii.weatherintegrationapi.config.meta.WeatherProviders;
import dev.drewboiii.weatherintegrationapi.weatherprovider.WeatherProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Component("WEATHER_COM")
public class WeatherComWeatherProvider implements WeatherProvider {

    private static final String WEATHER_COM_API_URL = "https://api.weatherapi.com/v1/forecast.json";

    public static final String KEY_PARAM = "key";
    public static final String LOCALITY_PARAM = "q";
    public static final String LANGUAGE_PARAM = "lang";

    private static final Map<String, String> AVAILABLE_LANGUAGES =
            Map.of(SupportedLanguages.RUSSIAN.name().toLowerCase(), "ru", SupportedLanguages.ENGLISH.name().toLowerCase(), "en");

    @Value("${weather-com.api.key}")
    private String weatherComApiKey;

    private final RestTemplate restTemplate;

    public WeatherComWeatherProvider(@Qualifier("PlainRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherNowDto getCurrent(Location location) throws WeatherException {
        return this.getCurrent(location, AVAILABLE_LANGUAGES.get(SupportedLanguages.ENGLISH.name()));
    }

    @Override
    public WeatherNowDto getCurrent(Location location, String language) throws WeatherException {
        String uri = UriComponentsBuilder.fromUriString(WEATHER_COM_API_URL)
                .queryParam(KEY_PARAM, weatherComApiKey)
                .queryParam(LOCALITY_PARAM, location.getLocality())
                .queryParam(LANGUAGE_PARAM, language)
//                .queryParam("days", "3")
//                .queryParam("aqi", "no")
//                .queryParam("alert", "no")
                .buildAndExpand()
                .toUriString();

        WeatherApiComResponseDto weatherApiComResponseDto;
        try {
            log.info("Send request to WeatherCom API for location {}", location.getLocality());
            weatherApiComResponseDto = restTemplate.getForObject(uri, WeatherApiComResponseDto.class);
        } catch (RestClientException ex) {
            log.error("WeatherApi.com responded with an error {}", ex.getMessage());
            throw new WeatherException(ex);
        }

        // TODO: 6/10/2022
        return WeatherNowDto.builder()
                .provider(WeatherProviders.WEATHER_COM)
                .location(weatherApiComResponseDto.getLocation().getName())
                .build();
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
                        .build(),
                WeatherLocations.SAMARA.name().toLowerCase(), Location.builder()
                        .country("Russia")
                        .locality("Samara")
                        .build()

        );
    }
}
