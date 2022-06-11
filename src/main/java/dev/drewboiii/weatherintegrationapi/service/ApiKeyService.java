package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.response.ApiKeyResponseDto;
import dev.drewboiii.weatherintegrationapi.model.ApiKey;
import dev.drewboiii.weatherintegrationapi.model.WeatherAuthApiKey;
import dev.drewboiii.weatherintegrationapi.persistence.ApiKeyRepository;
import dev.drewboiii.weatherintegrationapi.persistence.projection.ApiKeyInlineDetailsProjection;
import dev.drewboiii.weatherintegrationapi.persistence.projection.ApiKeyShortDetailsProjection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@AllArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyGeneratorService apiKeyGeneratorService;

    public ApiKeyResponseDto generate(ApiKeyRequestDto apiKeyRequestDto) {
        String generatedApiKey = apiKeyGeneratorService.generate();
        WeatherAuthApiKey weatherAuthApiKey = new WeatherAuthApiKey(generatedApiKey, AuthorityUtils.NO_AUTHORITIES);
        String email = apiKeyRequestDto.getEmail();
        ApiKey apiKey = ApiKey.builder()
                .content(weatherAuthApiKey.getPrincipal())
                .email(email)
                .build();

        log.info("Generating API Key for email - {}", email);
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
        log.info("Getting API Key details for the key - {}", apiKey);
        ApiKeyShortDetailsProjection projection = apiKeyRepository.getByContent(apiKey);
        LocalDateTime validUntil = projection.getValidUntil();
        return ApiKeyResponseDto.builder()
                .apiKey(projection.getContent())
                .mail(projection.getEmail())
                .validUntil(validUntil)
                .validInDays(LocalDateTime.now().until(validUntil, ChronoUnit.DAYS))
                .build();
    }

    public String getDetailsInline(String apiKey, String email) {
        log.info("Getting API Key details for the key - {} and email - {}", apiKey, email);
        ApiKeyInlineDetailsProjection contentInline = apiKeyRepository.getByContentAndEmail(apiKey, email);
        return contentInline.getInlineDetails();
    }

}
