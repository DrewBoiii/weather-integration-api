package dev.drewboiii.mailservice.service;

import dev.drewboiii.mailservice.dto.kafka.MailConsumedDto;
import dev.drewboiii.mailservice.dto.kafka.MailResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.mail.MailException;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MailFacade {

    MailService mailService;
    KafkaProducerService kafkaProducerService;

    public void handleMail(MailConsumedDto mailConsumedDto, String replyToTopic) {
        MailResponseDto dto = MailResponseDto.builder()
                .email(mailConsumedDto.getEmailTo())
                .payload(mailConsumedDto.getPayload())
                .subject(mailConsumedDto.getSubject())
                .build();

        try {
            mailService.send(mailConsumedDto.getEmailTo(), mailConsumedDto.getSubject(), mailConsumedDto.getPayload());
            dto.setIsDelivered(true);
        } catch (MailException e) {
            dto.setIsDelivered(false);
            dto.setError(e.getMessage());
        }

        Message<MailResponseDto> message = MessageBuilder
                .withPayload(dto)
                .setHeader(KafkaHeaders.TOPIC, replyToTopic)
                .build();

        ListenableFuture<SendResult<String, Object>> kafkaResponse = kafkaProducerService.sendEmailMessage(message);

        kafkaResponse.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Message to 'mail-service-reply-topic' wasn't delivered cause {}", ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Message to 'mail-service-reply-topic' was successfully delivered");
            }
        });
    }

}
