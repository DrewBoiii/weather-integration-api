package dev.drewboiii.weatherintegrationapi.persistence.projection;

import java.time.LocalDateTime;

public interface ApiKeyShortDetailsProjection {

    String getContent();
    String getEmail();
    LocalDateTime getValidUntil();

}
