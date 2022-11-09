package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.config.meta.MailDeliveringStatuses;
import dev.drewboiii.weatherintegrationapi.dto.kafka.EmailReplyConsumedDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MailFacade {

    EmailMessageService emailMessageService;

    public void handleMail(EmailReplyConsumedDto dto) {
        Boolean isDelivered = dto.getIsDelivered();
        String email = dto.getEmail();
        if (isDelivered) {
            log.info("Email was delivered to {}", email);
            emailMessageService.updateStatusByEmail(email, MailDeliveringStatuses.DELIVERED);
        } else {
            log.warn("Email wasn't delivered to {} cause {}", email, dto.getError());
            emailMessageService.updateStatusByEmail(email, MailDeliveringStatuses.NOT_DELIVERED);
            // TODO: 11/9/2022 save failed messages with subject, payload and ...
        }
    }

}
