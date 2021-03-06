package dev.drewboiii.weatherintegrationapi.weatherprovider;

import dev.drewboiii.weatherintegrationapi.dto.response.WeatherNowDto;
import dev.drewboiii.weatherintegrationapi.exception.WeatherException;
import dev.drewboiii.weatherintegrationapi.model.Location;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component("OPEN_WEATHER")
public class OpenWeatherProvider implements WeatherProvider {

    private static final String OPEN_WEATHER_API_URL = "";

    @Value("${open-weather.api.key}")
    private String openWeatherApiKey;

    private final RestTemplate restTemplate;

    public OpenWeatherProvider(@Qualifier("PlainRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherNowDto getCurrent(Location location, String language) throws WeatherException {
        String uri = UriComponentsBuilder.fromUriString(OPEN_WEATHER_API_URL)
                .queryParam("appid", openWeatherApiKey)
                .queryParam("lat", location.getLatitude())
                .queryParam("lon", location.getLongitude())
                .queryParam("lang", language)
                .queryParam("units", "metric")
                .buildAndExpand()
                .toUriString();
        // TODO: 6/10/2022
        return WeatherProvider.super.getCurrent(location);
    }

}
