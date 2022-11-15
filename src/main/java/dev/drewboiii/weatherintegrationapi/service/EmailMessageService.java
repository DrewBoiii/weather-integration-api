package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.config.meta.ApiKeyMailSubjects;
import dev.drewboiii.weatherintegrationapi.config.meta.MailDeliveringStatuses;
import dev.drewboiii.weatherintegrationapi.model.EmailMessage;
import dev.drewboiii.weatherintegrationapi.persistence.EmailMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailMessageService {

    private final EmailMessageRepository emailMessageRepository;

    public void updateStatusByEmailAndSubject(String email, MailDeliveringStatuses status, ApiKeyMailSubjects subject) {
        EmailMessage emailMessage = emailMessageRepository.getByEmailInfo_EmailAndSubject(email, subject);
        UUID uuid = emailMessage.getUuid();

        int updateStatus = emailMessageRepository.updateStatus(uuid, status);

        if (updateStatus != 1) {
            log.warn("Email message status wasn't updated - {}", email);
        }
    }

    public void save(EmailMessage emailMessage) {
        emailMessageRepository.save(emailMessage);
    }

}
