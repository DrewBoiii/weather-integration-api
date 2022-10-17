package dev.drewboiii.weatherintegrationapi.exception;

public class TooManyRequestsToApiException extends RuntimeException {

    public TooManyRequestsToApiException(String message) {
        super(message);
    }

}
