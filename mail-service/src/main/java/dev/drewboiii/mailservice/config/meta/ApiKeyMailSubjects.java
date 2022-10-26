package dev.drewboiii.mailservice.config.meta;

public enum ApiKeyMailSubjects {

    GENERATE ("Generate API Key"),
    REFRESH ("Refresh API Key");

    private final String subject;

    ApiKeyMailSubjects(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return subject;
    }

}
