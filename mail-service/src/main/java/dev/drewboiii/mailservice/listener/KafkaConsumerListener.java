package dev.drewboiii.mailservice.listener;

import dev.drewboiii.mailservice.dto.kafka.MailConsumedDto;
import dev.drewboiii.mailservice.service.MailFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerListener {

    private final MailFacade mailFacade;

    @KafkaListener(topics = "mail-service-topic", groupId = "consumer-group", containerFactory = "kafkaJsonListenerContainerFactory")
    public void mailServiceTopic(MailConsumedDto dto, @Header(KafkaHeaders.REPLY_TOPIC) String replyTo) {
        log.info("Received message from 'mail-service-topic' {}", dto);
        mailFacade.handleMail(dto, replyTo);
    }

}
