package dev.drewboiii.weatherintegrationapi.persistence;

import dev.drewboiii.weatherintegrationapi.exception.ApiKeyNotFoundException;
import dev.drewboiii.weatherintegrationapi.model.ApiKey;
import dev.drewboiii.weatherintegrationapi.persistence.projection.ApiKeyInlineDetailsProjection;
import dev.drewboiii.weatherintegrationapi.persistence.projection.ApiKeyShortDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID>, JpaSpecificationExecutor<ApiKey> {

    Optional<ApiKey> findApiKeyByContent(String apiKey);

    ApiKeyShortDetailsProjection getByContent(String key);

    default ApiKeyShortDetailsProjection getByContentOrThrow(String key) {
        return Optional.ofNullable(getByContent(key))
                .orElseThrow(() -> new ApiKeyNotFoundException("API Key - " + key + " wasn't found!"));
    }

    ApiKeyInlineDetailsProjection getByContentAndEmailInfo_Email(String key, String email);

    Boolean existsByEmailInfo_Email(String email);

    <T> T findByUuid(String uuid, Class<T> type);

}
