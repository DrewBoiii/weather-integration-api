package dev.drewboiii.weatherintegrationapi.config.meta;

public enum ApiKeyMailSubjects {

    GENERATE ("New API Key was generated - %s, valid %s days"),
    REFRESH ("API Key was refreshed. New API Key - %s, valid %s days");

    private final String message;

    ApiKeyMailSubjects(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
