package dev.drewboiii.weatherintegrationapi.controller;

import dev.drewboiii.weatherintegrationapi.exception.WeatherException;
import dev.drewboiii.weatherintegrationapi.exception.WeatherNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class WeatherExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(WeatherNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    String weatherNotFoundHandler(WeatherNotFoundException ex) {
        String errorMessage = ex.getMessage();
        log.error(errorMessage, ex);
        return errorMessage;
    }

    @ResponseBody
    @ExceptionHandler(WeatherException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    String weatherExceptionHandler(WeatherException ex) {
        String errorMessage = ex.getMessage();
        log.error(errorMessage, ex);
        return errorMessage;
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    String weatherExceptionHandler(IllegalArgumentException ex) {
        String errorMessage = ex.getMessage();
        log.error(errorMessage, ex);
        return errorMessage;
    }

}