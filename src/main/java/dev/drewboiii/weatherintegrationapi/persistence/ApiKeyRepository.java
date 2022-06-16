package dev.drewboiii.weatherintegrationapi.persistence;

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

    ApiKeyInlineDetailsProjection getByContentAndEmail(String key, String email);

    <T> T findByUuid(String uuid, Class<T> type);

}
