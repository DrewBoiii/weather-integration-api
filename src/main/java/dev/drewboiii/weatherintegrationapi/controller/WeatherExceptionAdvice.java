package dev.drewboiii.weatherintegrationapi.controller;

import dev.drewboiii.weatherintegrationapi.exception.ApiKeyNotFoundException;
import dev.drewboiii.weatherintegrationapi.exception.TooManyRequestsToApiException;
import dev.drewboiii.weatherintegrationapi.exception.WeatherException;
import dev.drewboiii.weatherintegrationapi.exception.WeatherNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class WeatherExceptionAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    String handleAllExceptions(Exception ex, WebRequest request) {
        String errorMessage = ex.getMessage();
        log.error(errorMessage, ex);
        return "Server error";
    }

    @ExceptionHandler({WeatherNotFoundException.class, ApiKeyNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    String notFoundHandler(RuntimeException ex) {
        String errorMessage = ex.getMessage();
        log.error(errorMessage, ex);
        return errorMessage;
    }

    @ExceptionHandler({WeatherException.class, IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    String badRequestHandler(RuntimeException ex) {
        String errorMessage = ex.getMessage();
        log.error(errorMessage, ex);
        return errorMessage;
    }

    @ExceptionHandler(TooManyRequestsToApiException.class)
    @ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
    String tooManyRequestsHandler(TooManyRequestsToApiException ex) {
        String errorMessage = ex.getMessage();
        log.error(errorMessage, ex);
        return errorMessage;
    }

    @ExceptionHandler(MailException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    String mailExceptionHandler(MailException ex) {
        String errorMessage = ex.getMessage();
        log.error(errorMessage, ex);
        return errorMessage;
    }

}
