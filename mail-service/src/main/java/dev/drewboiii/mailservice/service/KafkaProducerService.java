package dev.drewboiii.mailservice.service;

import dev.drewboiii.mailservice.dto.kafka.MailResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ListenableFuture<SendResult<String, Object>> sendEmailMessage(Message<MailResponseDto> message) {
        return kafkaTemplate.send(message);
    }

}
