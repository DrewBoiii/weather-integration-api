package dev.drewboiii.weatherintegrationapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    private static final String YANDEX_WEATHER_API_KEY_HEADER = "X-Yandex-API-Key";

    @Value("${application.weather-providers.yandex-weather.api-key}")
    private String yandexWeatherApiKey;

    @Bean("YandexRestTemplate")
    public RestTemplate yandexRestTemplate() {
        return new RestTemplateBuilder()
                .defaultHeader(YANDEX_WEATHER_API_KEY_HEADER, yandexWeatherApiKey)
                .build();
    }

    @Bean
    @Primary
    public RestTemplate plainRestTemplate() {
        return new RestTemplateBuilder().build();
    }

}
