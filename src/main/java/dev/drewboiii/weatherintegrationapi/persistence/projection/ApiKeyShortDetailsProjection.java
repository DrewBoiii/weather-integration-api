package dev.drewboiii.weatherintegrationapi.persistence.projection;

import dev.drewboiii.weatherintegrationapi.model.EmailInfo;

import java.time.LocalDateTime;

public interface ApiKeyShortDetailsProjection {

    String getContent();
    EmailInfo getEmailInfo();
    LocalDateTime getValidUntil();

}
