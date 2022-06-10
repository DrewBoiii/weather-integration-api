package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.dto.response.WeatherNowDto;
import dev.drewboiii.weatherintegrationapi.model.WeatherLocation;
import dev.drewboiii.weatherintegrationapi.model.WeatherProviderLanguage;
import dev.drewboiii.weatherintegrationapi.model.WeatherProviders;
import dev.drewboiii.weatherintegrationapi.weatherprovider.WeatherProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.EnumUtils;

import java.util.Map;

@Service
@AllArgsConstructor
public class WeatherService {

    private Map<String, WeatherProvider> weatherProviderMap;

    public WeatherNowDto getCurrent(String provider, String location) {
        WeatherProviders weatherProvider = EnumUtils.findEnumInsensitiveCase(WeatherProviders.class, provider);
        WeatherLocation weatherLocation = EnumUtils.findEnumInsensitiveCase(WeatherLocation.class, location);

        return weatherProviderMap.get(weatherProvider.name())
                .getCurrent(weatherLocation);
    }

    public WeatherNowDto getCurrent(String provider, String location, String language) {
        WeatherProviders weatherProvider = EnumUtils.findEnumInsensitiveCase(WeatherProviders.class, provider);
        WeatherLocation weatherLocation = EnumUtils.findEnumInsensitiveCase(WeatherLocation.class, location);
        WeatherProviderLanguage weatherProviderLanguage = EnumUtils.findEnumInsensitiveCase(WeatherProviderLanguage.class, language);

        return weatherProviderMap.get(weatherProvider.name())
                .getCurrent(weatherLocation, weatherProviderLanguage);
    }

}
