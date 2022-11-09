package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.dto.kafka.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public ListenableFuture<SendResult<String, Object>> sendEmailMessage(String to, String subject, String payload) {
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

        return kafkaTemplate.send(message);
    }

}
