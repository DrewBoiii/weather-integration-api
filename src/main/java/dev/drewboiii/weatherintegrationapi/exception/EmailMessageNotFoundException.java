package dev.drewboiii.weatherintegrationapi.exception;

public class EmailMessageNotFoundException extends RuntimeException {

    public EmailMessageNotFoundException(String message) {
        super(message);
    }

}
