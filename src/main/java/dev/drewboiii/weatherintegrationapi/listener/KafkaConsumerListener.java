package dev.drewboiii.weatherintegrationapi.listener;

import dev.drewboiii.weatherintegrationapi.dto.kafka.EmailReplyConsumedDto;
import dev.drewboiii.weatherintegrationapi.service.MailFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerListener {

    private final MailFacade mailFacade;

    @KafkaListener(topics = "mail-service-reply-topic", groupId = "consumer-group", containerFactory = "kafkaJsonListenerContainerFactory")
    public void mailServiceReplyTopic(EmailReplyConsumedDto dto) {
        log.info("Received message from 'mail-service-reply-topic' {}", dto);
        mailFacade.handleMail(dto);
    }

}
