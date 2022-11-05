package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.config.meta.MailDeliveringStatuses;
import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRefreshRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.response.ApiKeyResponseDto;
import dev.drewboiii.weatherintegrationapi.config.meta.ApiKeyMailSubjects;
import dev.drewboiii.weatherintegrationapi.assembler.ApiKeyResponseDtoModelAssembler;
import dev.drewboiii.weatherintegrationapi.model.EmailMessage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.support.SendResult;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ApiKeyFacade {

    ApiKeyService apiKeyService;
    KafkaProducerService kafkaProducerService;
    EmailMessageService emailMessageService;
    ApiKeyResponseDtoModelAssembler modelAssembler;

    public EntityModel<ApiKeyResponseDto> generate(ApiKeyRequestDto apiKeyRequestDto) {
        ApiKeyResponseDto apiKeyResponseDto = apiKeyService.generate(apiKeyRequestDto);

        String email = apiKeyRequestDto.getEmail();
        ListenableFuture<SendResult<String, Object>> kafkaResponse = kafkaProducerService.sendEmailMessage(
                email,
                ApiKeyMailSubjects.GENERATE.name(),
                String.format(ApiKeyMailSubjects.GENERATE.getMessage(), apiKeyResponseDto.getApiKey(), apiKeyResponseDto.getValidInDays()));
        kafkaResponse.addCallback(new KafkaSendCallback<>() {

            @Override
            public void onFailure(KafkaProducerException ex) {
                log.error("Message to 'mail-service-topic' wasn't delivered cause {}", ex.getMessage());
                emailMessageService.updateStatusByEmail(email, MailDeliveringStatuses.FAILED);
            }

            @Override
            public void onSuccess(SendResult<String, Object> ignore) {
                log.info("Message to 'mail-service-topic' was successfully delivered");
                emailMessageService.updateStatusByEmail(email, MailDeliveringStatuses.ONGOING);
            }

        });

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
