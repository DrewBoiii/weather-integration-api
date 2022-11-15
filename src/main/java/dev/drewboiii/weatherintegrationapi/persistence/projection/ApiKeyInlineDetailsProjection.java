package dev.drewboiii.weatherintegrationapi.persistence.projection;

import org.springframework.beans.factory.annotation.Value;

public interface ApiKeyInlineDetailsProjection {

    @Value("#{target.content + ', ' + target.emailInfo.email + ', ' + target.validUntil}")
    String getInlineDetails();

}
