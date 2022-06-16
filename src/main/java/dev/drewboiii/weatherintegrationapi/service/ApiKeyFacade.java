package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRefreshRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.response.ApiKeyResponseDto;
import dev.drewboiii.weatherintegrationapi.model.ApiKeyMailSubject;
import dev.drewboiii.weatherintegrationapi.model.assembler.ApiKeyResponseDtoModelAssembler;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ApiKeyFacade {

    private final ApiKeyService apiKeyService;
    private final MailService mailService;
    private final ApiKeyResponseDtoModelAssembler modelAssembler;

    public EntityModel<ApiKeyResponseDto> generate(ApiKeyRequestDto apiKeyRequestDto) {
        ApiKeyResponseDto apiKeyResponseDto = apiKeyService.generate(apiKeyRequestDto);
        mailService.send(apiKeyRequestDto.getEmail(),
                ApiKeyMailSubject.GENERATE.name(),
                String.format(ApiKeyMailSubject.GENERATE.getMessage(), apiKeyResponseDto.getApiKey(), apiKeyResponseDto.getValidInDays()));
        return modelAssembler.toModel(apiKeyResponseDto);
    }

    public EntityModel<ApiKeyResponseDto> refresh(ApiKeyRefreshRequestDto apiKeyRefreshRequestDto) {
        ApiKeyResponseDto apiKeyResponseDto = apiKeyService.refresh(apiKeyRefreshRequestDto);
        mailService.send(apiKeyRefreshRequestDto.getEmail(),
                ApiKeyMailSubject.REFRESH.name(),
                String.format(ApiKeyMailSubject.REFRESH.getMessage(), apiKeyResponseDto.getApiKey(), apiKeyResponseDto.getValidInDays()));
        return modelAssembler.toModel(apiKeyResponseDto);
    }

    public EntityModel<ApiKeyResponseDto> details(String apiKey) {
        ApiKeyResponseDto responseDto = apiKeyService.getDetails(apiKey);
        return modelAssembler.toModel(responseDto);
    }

}
