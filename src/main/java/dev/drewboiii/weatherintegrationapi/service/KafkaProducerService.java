package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.dto.request.kafka.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEmailMessage(String to, String subject, String payload) {
        EmailDto dto = EmailDto.builder()
                .emailTo(to)
                .subject(subject)
                .payload(payload)
                .build();

        Message<EmailDto> message = MessageBuilder
                .withPayload(dto)
                .setHeader(KafkaHeaders.REPLY_TOPIC, "mail-service-reply-topic")
                .setHeader(KafkaHeaders.TOPIC, "mail-service-topic") // TODO: 10/27/2022 extract from config?
                .build();

        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(message);
        future.addCallback(new KafkaSendCallback<>() {
            @Override
            public void onFailure(KafkaProducerException ex) {
                log.error("Message wasn't delivered cause {}", ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("Message to 'mail-service-topic' was successfully delivered");
            }
        });
    }

}
