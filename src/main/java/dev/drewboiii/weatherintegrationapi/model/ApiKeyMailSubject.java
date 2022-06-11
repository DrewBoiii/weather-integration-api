package dev.drewboiii.weatherintegrationapi.model;

public enum ApiKeyMailSubject {

    GENERATE ("New API Key was generated."),
    REFRESH ("API Key was refreshed.");

    private final String message;

    ApiKeyMailSubject(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
