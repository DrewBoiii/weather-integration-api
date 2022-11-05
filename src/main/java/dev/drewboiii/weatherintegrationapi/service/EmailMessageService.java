package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.config.meta.MailDeliveringStatuses;
import dev.drewboiii.weatherintegrationapi.model.EmailMessage;
import dev.drewboiii.weatherintegrationapi.persistence.EmailMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailMessageService {

    private final EmailMessageRepository emailMessageRepository;

    public EmailMessage getByEmail(String email) {
        return emailMessageRepository.getByEmailOrThrow(email);
    }

    public void updateStatusByEmail(String email, MailDeliveringStatuses status) {
        int responseStatus = emailMessageRepository.updateStatus(email, status);

        if (responseStatus != 1) {
            log.warn("Email message {} wasn't updated", email);
        }
    }

}
