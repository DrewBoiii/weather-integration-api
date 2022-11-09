package dev.drewboiii.weatherintegrationapi.config.meta;

public enum MailDeliveringStatuses {

    /**
     * Default status
     */
    NONE,

    /**
     * Kafka didn't received email message
     */
    FAILED,

    /**
     * Kafka received email message, but message wasn't send by a service yet
     */
    ONGOING,

    /**
     * Kafka received successful mail delivering reply message from a service
     */
    DELIVERED,

    /**
     * Kafka received failed mail delivering reply message from a service
     */
    NOT_DELIVERED

}
