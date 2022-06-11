package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRefreshRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.response.ApiKeyResponseDto;
import dev.drewboiii.weatherintegrationapi.model.ApiKeyMailSubject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ApiKeyFacade {

    private final ApiKeyService apiKeyService;
    private final MailService mailService;

    public ApiKeyResponseDto generateApiKey(ApiKeyRequestDto apiKeyRequestDto) {
        ApiKeyResponseDto apiKeyResponseDto = apiKeyService.generate(apiKeyRequestDto);
        mailService.send(apiKeyRequestDto.getEmail(), ApiKeyMailSubject.GENERATE, apiKeyResponseDto.getApiKey());
        return apiKeyResponseDto;
    }

    public ApiKeyResponseDto refreshApiKey(ApiKeyRefreshRequestDto apiKeyRefreshRequestDto) {
        ApiKeyResponseDto apiKeyResponseDto = apiKeyService.refresh(apiKeyRefreshRequestDto);
        mailService.send(apiKeyRefreshRequestDto.getEmail(), ApiKeyMailSubject.REFRESH, apiKeyResponseDto.getApiKey());
        return apiKeyResponseDto;
    }

}
