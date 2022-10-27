package dev.drewboiii.mailservice.listener;

import dev.drewboiii.mailservice.dto.kafka.MailDto;
import dev.drewboiii.mailservice.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerListener {

    private final MailService mailService;

    @KafkaListener(topics = "mail-service-topic", groupId = "consumer-group", containerFactory = "kafkaJsonListenerContainerFactory")
    public void consume(MailDto mailDto) {
        log.info("Received message from 'mail-service-topic' {}", mailDto);
        mailService.send(mailDto.getEmailTo(), mailDto.getSubject(), mailDto.getPayload());
    }

}
