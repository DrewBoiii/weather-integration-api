package dev.drewboiii.weatherintegrationapi.weatherprovider;

import dev.drewboiii.weatherintegrationapi.dto.response.WeatherForecastDto;
import dev.drewboiii.weatherintegrationapi.dto.response.WeatherNowDto;
import dev.drewboiii.weatherintegrationapi.dto.response.WeatherTodayDto;
import dev.drewboiii.weatherintegrationapi.exception.WeatherException;
import dev.drewboiii.weatherintegrationapi.model.WeatherLocation;
import dev.drewboiii.weatherintegrationapi.model.WeatherProviderLanguage;

public interface WeatherProvider {

    /**
     * Returns weather at current time
     *
     * @param location name of the city
     * @return WeatherNowDto
     */
    default WeatherNowDto getCurrent(WeatherLocation location) throws WeatherException {
        return WeatherNowDto.builder().build();
    }

    default WeatherNowDto getCurrent(WeatherLocation location, WeatherProviderLanguage language) throws WeatherException {
        return WeatherNowDto.builder().build();
    }

    default WeatherTodayDto getToday(WeatherLocation location) throws WeatherException {
        return WeatherTodayDto.builder().build();
    }

    default WeatherForecastDto getForecast(WeatherLocation location, int days) throws WeatherException {
        return WeatherForecastDto.builder().build();
    }

//    Map<String, String> getAvailableLanguages();
//
//    Map<String, Location> getAvailableLocations();

}
