package dev.drewboiii.weatherintegrationapi.persistence.projection;

import dev.drewboiii.weatherintegrationapi.model.EmailMessage;

import java.time.LocalDateTime;

public interface ApiKeyShortDetailsProjection {

    String getContent();
    EmailMessage getEmailMessage();
    LocalDateTime getValidUntil();

}
