package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.model.EmailInfo;
import dev.drewboiii.weatherintegrationapi.persistence.EmailInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailInfoService {

    private final EmailInfoRepository emailInfoRepository;

    public EmailInfo getByEmail(String email) {
        return emailInfoRepository.getByEmailOrThrow(email);
    }

    public void updateEmail(String oldEmail, String newEmail) {
        int responseStatus = emailInfoRepository.updateEmail(oldEmail, newEmail);

        if (responseStatus != 1) {
            log.warn("Email info {} wasn't updated", oldEmail);
        }
    }

    public void save(EmailInfo emailInfo) {
        emailInfoRepository.save(emailInfo);
    }
}
