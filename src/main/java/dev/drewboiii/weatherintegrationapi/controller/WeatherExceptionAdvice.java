package dev.drewboiii.weatherintegrationapi.controller;

import dev.drewboiii.weatherintegrationapi.exception.ApiKeyNotFoundException;
import dev.drewboiii.weatherintegrationapi.exception.TooManyRequestsToApiException;
import dev.drewboiii.weatherintegrationapi.exception.WeatherException;
import dev.drewboiii.weatherintegrationapi.exception.WeatherNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
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
    @ExceptionHandler({WeatherNotFoundException.class, ApiKeyNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    String notFoundHandler(RuntimeException ex) {
        String errorMessage = ex.getMessage();
        log.error(errorMessage, ex);
        return errorMessage;
    }

    @ResponseBody
    @ExceptionHandler({WeatherException.class, TooManyRequestsToApiException.class, IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    String badRequestHandler(RuntimeException ex) {
        String errorMessage = ex.getMessage();
        log.error(errorMessage, ex);
        return errorMessage;
    }

    @ResponseBody
    @ExceptionHandler(MailException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    String mailExceptionHandler(MailException ex) {
        String errorMessage = ex.getMessage();
        log.error(errorMessage, ex);
        return errorMessage;
    }

}
