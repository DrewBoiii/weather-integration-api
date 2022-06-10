package dev.drewboiii.weatherintegrationapi.exception;

public class WeatherNotFoundException extends RuntimeException {

    public WeatherNotFoundException() {
    }

    public WeatherNotFoundException(String message) {
        super(message);
    }
}
