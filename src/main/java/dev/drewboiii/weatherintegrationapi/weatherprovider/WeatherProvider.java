package dev.drewboiii.weatherintegrationapi.weatherprovider;

import dev.drewboiii.weatherintegrationapi.dto.response.WeatherForecastDto;
import dev.drewboiii.weatherintegrationapi.dto.response.WeatherNowDto;
import dev.drewboiii.weatherintegrationapi.dto.response.WeatherTodayDto;
import dev.drewboiii.weatherintegrationapi.exception.WeatherException;
import dev.drewboiii.weatherintegrationapi.model.Location;

import java.util.Map;

public interface WeatherProvider {

    /**
     * Returns weather at current time
     *
     * @param location name of the city
     * @return WeatherNowDto
     */
    default WeatherNowDto getCurrent(Location location) throws WeatherException {
        return WeatherNowDto.builder().build();
    }

    default WeatherNowDto getCurrent(Location location, String language) throws WeatherException {
        return WeatherNowDto.builder().build();
    }

    default WeatherTodayDto getToday(Location location) throws WeatherException {
        return WeatherTodayDto.builder().build();
    }

    default WeatherForecastDto getForecast(Location location, int days) throws WeatherException {
        return WeatherForecastDto.builder().build();
    }

    default Map<String, String> getAvailableLanguages() {
        return Map.of();
    }

    default Map<String, Location> getAvailableLocations() {
        return Map.of();
    }

}
