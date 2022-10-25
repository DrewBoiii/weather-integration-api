package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRefreshRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.response.ApiKeyResponseDto;
import dev.drewboiii.weatherintegrationapi.exception.ApiKeyNotFoundException;
import dev.drewboiii.weatherintegrationapi.model.ApiKey;
import dev.drewboiii.weatherintegrationapi.model.WeatherAuthApiKey;
import dev.drewboiii.weatherintegrationapi.persistence.ApiKeyRepository;
import dev.drewboiii.weatherintegrationapi.persistence.projection.ApiKeyInlineDetailsProjection;
import dev.drewboiii.weatherintegrationapi.persistence.projection.ApiKeyShortDetailsProjection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.JoinType;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyGeneratorService apiKeyGeneratorService;
    private final NamedParameterJdbcOperations template;

    public ApiKeyResponseDto generate(ApiKeyRequestDto apiKeyRequestDto) {
        String generatedApiKey = apiKeyGeneratorService.generate();
        WeatherAuthApiKey weatherAuthApiKey = new WeatherAuthApiKey(generatedApiKey, AuthorityUtils.NO_AUTHORITIES);
        String email = apiKeyRequestDto.getEmail();
        ApiKey apiKey = ApiKey.builder()
                .content(weatherAuthApiKey.getPrincipal())
                .email(email)
                .build();

        ApiKey saved = apiKeyRepository.save(apiKey);

        LocalDateTime validUntil = saved.getValidUntil();
        return ApiKeyResponseDto.builder()
                .apiKey(saved.getContent())
                .mail(saved.getEmail())
                .createdAt(saved.getCreatedAt())
                .validUntil(validUntil)
                .validInDays(LocalDateTime.now().until(validUntil, ChronoUnit.DAYS))
                .build();
    }

    public ApiKeyResponseDto getDetails(String apiKey) {
        ApiKeyShortDetailsProjection projection = apiKeyRepository.getByContentOrThrow(apiKey);
        LocalDateTime validUntil = projection.getValidUntil();
        return ApiKeyResponseDto.builder()
                .apiKey(projection.getContent())
                .mail(projection.getEmail())
                .validUntil(validUntil)
                .validInDays(LocalDateTime.now().until(validUntil, ChronoUnit.DAYS))
                .build();
    }

    public String getDetailsInline(String apiKey, String email) {
        ApiKeyInlineDetailsProjection contentInline = apiKeyRepository.getByContentAndEmail(apiKey, email);
        return contentInline.getInlineDetails();
    }

    public ApiKeyResponseDto refresh(ApiKeyRefreshRequestDto apiKeyRefreshRequestDto) {
        String oldKey = apiKeyRefreshRequestDto.getKey();
        String email = apiKeyRefreshRequestDto.getEmail();

        ApiKeyShortDetailsProjection oldKeyByContent = Optional.ofNullable(apiKeyRepository.getByContent(oldKey))
                .orElseThrow(() -> new ApiKeyNotFoundException("API Key - " + oldKey + " wasn't found!"));

        LocalDateTime validUntil = oldKeyByContent.getValidUntil();

        if (validUntil.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("API Key is present!");
        }

        String generatedKey = apiKeyGeneratorService.generate();

        LocalDateTime newValidUntilValue = LocalDateTime.now().plusDays(7L);
        Integer responseStatus = template.execute(
                "UPDATE api_key SET created_at = :createdAt, valid_until = :validUntil, content = :content WHERE content = :oldKey AND email = :email",
                Map.of(
                        "createdAt", LocalDateTime.now(),
                        "validUntil", newValidUntilValue,
                        "content", generatedKey,
                        "email", email,
                        "oldKey", oldKey),
                PreparedStatement::executeUpdate
        );

        if (responseStatus == null || responseStatus != 1) {
            throw new IllegalArgumentException("API Key wasn't updated!");
        }

        return ApiKeyResponseDto.builder()
                .apiKey(generatedKey)
                .mail(email)
                .validUntil(newValidUntilValue)
                .validInDays(LocalDateTime.now().until(newValidUntilValue, ChronoUnit.DAYS))
                .build();
    }

    public Set<ApiKey> getAllByEmails(Set<String> emails) {
        return new HashSet<>(
                apiKeyRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
                            root.fetch("requests", JoinType.INNER);
                            return criteriaBuilder.and(
                                    root.get("email").in(emails),
                                    criteriaBuilder.greaterThan(root.get("validUntil"), LocalDateTime.now())
                            );
                        }
                )
        );
    }

}
