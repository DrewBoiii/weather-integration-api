package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.model.ApiKeyMailSubject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String from;

    public void send(String to, ApiKeyMailSubject subject, String apiKey) {
        log.info("Sending email...");
        // TODO: 6/11/2022 sending logic
    }

}
