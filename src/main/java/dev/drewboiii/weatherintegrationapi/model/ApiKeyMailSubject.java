package dev.drewboiii.weatherintegrationapi.model;

public enum ApiKeyMailSubject {

    GENERATE ("New API Key was generated - %s, valid %s days"),
    REFRESH ("API Key was refreshed. New API Key - %s, valid %s days");

    private final String message;

    ApiKeyMailSubject(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
