package dev.drewboiii.weatherintegrationapi.weatherprovider;

import dev.drewboiii.weatherintegrationapi.dto.response.WeatherNowDto;
import dev.drewboiii.weatherintegrationapi.dto.weatherapicom.WeatherApiComResponseDto;
import dev.drewboiii.weatherintegrationapi.exception.WeatherException;
import dev.drewboiii.weatherintegrationapi.model.WeatherLocation;
import dev.drewboiii.weatherintegrationapi.model.WeatherProviderLanguage;
import dev.drewboiii.weatherintegrationapi.model.WeatherProviders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component("WEATHER_COM")
public class WeatherComWeatherProvider implements WeatherProvider {

    private static final String WEATHER_COM_API_URL = "https://api.weatherapi.com/v1/forecast.json";
    public static final String KEY_PARAM = "key";
    public static final String LOCALITY_PARAM = "q";
    public static final String LANGUAGE_PARAM = "lang";

    @Value("${weather-com.api.key}")
    private String weatherApiComKey;

    private final RestTemplate restTemplate;

    public WeatherComWeatherProvider(@Qualifier("PlainRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherNowDto getCurrent(WeatherLocation location) throws WeatherException {
        log.info("Send request to WeatherApi.com for location {}", location.name());

        String uri = UriComponentsBuilder.fromUriString(WEATHER_COM_API_URL)
                .queryParam(KEY_PARAM, weatherApiComKey) // injection issues
                .queryParam(LOCALITY_PARAM, location.getLocality())
                .queryParam(LANGUAGE_PARAM, WeatherProviderLanguage.ENGLISH.getWeatherComLang())
//                .queryParam("days", "3")
//                .queryParam("aqi", "no")
//                .queryParam("alert", "no")
                .buildAndExpand()
                .toUriString();

        WeatherApiComResponseDto weatherApiComResponseDto;
        try {
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

}
