package dev.drewboiii.weatherintegrationapi.controller;

import dev.drewboiii.weatherintegrationapi.exception.WeatherException;
import dev.drewboiii.weatherintegrationapi.exception.WeatherNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class WeatherExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    String handleAllExceptions(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        log.error(errorMessage, ex);
        return "Server error";
    }

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
