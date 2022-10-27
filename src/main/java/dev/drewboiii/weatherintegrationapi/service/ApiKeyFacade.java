package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRefreshRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.response.ApiKeyResponseDto;
import dev.drewboiii.weatherintegrationapi.config.meta.ApiKeyMailSubjects;
import dev.drewboiii.weatherintegrationapi.assembler.ApiKeyResponseDtoModelAssembler;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ApiKeyFacade {

    private final ApiKeyService apiKeyService;
    private final KafkaProducerService kafkaProducerService;
    private final ApiKeyResponseDtoModelAssembler modelAssembler;

    public EntityModel<ApiKeyResponseDto> generate(ApiKeyRequestDto apiKeyRequestDto) {
        ApiKeyResponseDto apiKeyResponseDto = apiKeyService.generate(apiKeyRequestDto);
        kafkaProducerService.sendEmailMessage(
                apiKeyRequestDto.getEmail(),
                ApiKeyMailSubjects.GENERATE.name(),
                String.format(ApiKeyMailSubjects.GENERATE.getMessage(), apiKeyResponseDto.getApiKey(), apiKeyResponseDto.getValidInDays()));
        return modelAssembler.toModel(apiKeyResponseDto);
    }

    public EntityModel<ApiKeyResponseDto> refresh(ApiKeyRefreshRequestDto apiKeyRefreshRequestDto) {
        ApiKeyResponseDto apiKeyResponseDto = apiKeyService.refresh(apiKeyRefreshRequestDto);
        kafkaProducerService.sendEmailMessage(
                apiKeyRefreshRequestDto.getEmail(),
                ApiKeyMailSubjects.REFRESH.name(),
                String.format(ApiKeyMailSubjects.REFRESH.getMessage(), apiKeyResponseDto.getApiKey(), apiKeyResponseDto.getValidInDays()));
        return modelAssembler.toModel(apiKeyResponseDto);
    }

    public EntityModel<ApiKeyResponseDto> details(String apiKey) {
        ApiKeyResponseDto responseDto = apiKeyService.getDetails(apiKey);
        return modelAssembler.toModel(responseDto);
    }

}
