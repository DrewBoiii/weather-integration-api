package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.dto.response.WeatherNowDto;
import dev.drewboiii.weatherintegrationapi.model.Location;
import dev.drewboiii.weatherintegrationapi.model.SupportedLanguages;
import dev.drewboiii.weatherintegrationapi.weatherprovider.WeatherProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class WeatherService {

    private Map<String, WeatherProvider> weatherProviderMap;

    public WeatherNowDto getCurrent(String provider, String locationName) {
        return this.getCurrent(provider, locationName, SupportedLanguages.ENGLISH.name());
    }

    public WeatherNowDto getCurrent(String provider, String locationName, String language) {
        WeatherProvider weatherProvider = weatherProviderMap.get(provider.toUpperCase());

        if (weatherProvider == null) {
            throw new IllegalArgumentException("Unsupported weather provider - " + provider);
        }

        Location location = weatherProvider.getAvailableLocations().get(locationName.toLowerCase());

        if (location == null) {
            throw new IllegalArgumentException("Unsupported location - " + locationName);
        }

        String lang = weatherProvider.getAvailableLanguages().get(language.toLowerCase());

        if (lang == null) {
            throw new IllegalArgumentException("Unsupported language - " + language);
        }

        return weatherProvider.getCurrent(location, lang);
    }

}
