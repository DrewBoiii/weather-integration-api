package dev.drewboiii.weatherintegrationapi.controller;

import dev.drewboiii.weatherintegrationapi.dto.response.WeatherNowDto;
import dev.drewboiii.weatherintegrationapi.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/weather")
public class WeatherController {

    private WeatherService weatherService;

    @GetMapping("/now")
    @ResponseStatus(HttpStatus.OK)
    public WeatherNowDto now(@RequestParam String provider,
                             @RequestParam String location,
                             @RequestParam(name = "lang", required = false) String language) {
        return language == null
                ? weatherService.getCurrent(provider, location)
                : weatherService.getCurrent(provider, location, language);
    }


//    @GetMapping("/today")
//    @ResponseStatus(HttpStatus.OK)
//    public YandexWeatherApiResponseDto today() {
//        return weatherService.get();
//    }
//
//    @GetMapping("/forecast")
//    @ResponseStatus(HttpStatus.OK)
//    public YandexWeatherApiResponseDto forecast() {
//        return weatherService.getWeather();
//    }

}
