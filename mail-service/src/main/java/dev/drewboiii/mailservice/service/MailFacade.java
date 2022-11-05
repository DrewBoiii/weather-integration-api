package dev.drewboiii.mailservice.service;

import dev.drewboiii.mailservice.dto.kafka.MailConsumedDto;
import dev.drewboiii.mailservice.dto.kafka.MailResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.mail.MailException;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MailFacade {

    MailService mailService;

    public void handleMail(MailConsumedDto mailConsumedDto, String replyTo) {
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
                .setHeader(KafkaHeaders.TOPIC, replyTo)
                .build();
        // TODO: 11/5/2022 kafka send response to replyToTopic
    }

}
