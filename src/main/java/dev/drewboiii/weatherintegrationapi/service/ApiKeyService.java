package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.config.meta.ApiKeyMailSubjects;
import dev.drewboiii.weatherintegrationapi.config.meta.MailDeliveringStatuses;
import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRefreshRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.response.ApiKeyResponseDto;
import dev.drewboiii.weatherintegrationapi.exception.ApiKeyNotFoundException;
import dev.drewboiii.weatherintegrationapi.model.ApiKey;
import dev.drewboiii.weatherintegrationapi.model.EmailInfo;
import dev.drewboiii.weatherintegrationapi.model.EmailMessage;
import dev.drewboiii.weatherintegrationapi.model.WeatherAuthApiKey;
import dev.drewboiii.weatherintegrationapi.persistence.ApiKeyRepository;
import dev.drewboiii.weatherintegrationapi.persistence.projection.ApiKeyInlineDetailsProjection;
import dev.drewboiii.weatherintegrationapi.persistence.projection.ApiKeyShortDetailsProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.JoinType;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.util.Collections.emptyList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyGeneratorService apiKeyGeneratorService;
    private final EmailInfoService emailInfoService;
    private final EmailMessageService emailMessageService;
    private final NamedParameterJdbcOperations template;

    public ApiKeyResponseDto generate(ApiKeyRequestDto dto) {
        String email = dto.getEmail();

        if (apiKeyRepository.existsByEmailInfo_Email(email)) {
            throw new RuntimeException("API Key already exists for this email");
        }

        String generatedApiKey = apiKeyGeneratorService.generate();

        ApiKey apiKey = buildApiKey(generatedApiKey, email);

        ApiKey saved = apiKeyRepository.save(apiKey);

        EmailInfo emailInfo = emailInfoService.getByEmail(email);

        EmailMessage emailMessage = EmailMessage.builder()
                .subject(ApiKeyMailSubjects.GENERATE)
                .status(MailDeliveringStatuses.NONE)
                .emailInfo(emailInfo)
                .build();
        emailMessageService.save(emailMessage);

        LocalDateTime validUntil = saved.getValidUntil();
        return ApiKeyResponseDto.builder()
                .apiKey(saved.getContent())
                .mail(saved.getEmailInfo().getEmail())
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
                .mail(projection.getEmailInfo().getEmail())
                .validUntil(validUntil)
                .validInDays(LocalDateTime.now().until(validUntil, ChronoUnit.DAYS))
                .build();
    }

    public String getDetailsInline(String apiKey, String email) {
        ApiKeyInlineDetailsProjection contentInline = apiKeyRepository.getByContentAndEmailInfo_Email(apiKey, email);
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
        // TODO: 11/10/2022
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

    private ApiKey buildApiKey(String apiKey, String email) {
        EmailInfo emailInfo = EmailInfo.builder()
                .email(email)
                .emailMessages(List.of())
                .build();

        return ApiKey.builder()
                .content(apiKey)
                .emailInfo(emailInfo)
                .build();
    }

}
