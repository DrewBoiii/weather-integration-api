package dev.drewboiii.weatherintegrationapi.persistence;

import dev.drewboiii.weatherintegrationapi.config.meta.ApiKeyMailSubjects;
import dev.drewboiii.weatherintegrationapi.config.meta.MailDeliveringStatuses;
import dev.drewboiii.weatherintegrationapi.exception.EmailMessageNotFoundException;
import dev.drewboiii.weatherintegrationapi.model.EmailMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailMessageRepository extends JpaRepository<EmailMessage, String> {

    @Modifying
    @Transactional
    @Query("UPDATE EmailMessage em SET em.status = :status WHERE em.uuid = :uuid")
    int updateStatus(@Param("uuid") UUID uuid, @Param("status") MailDeliveringStatuses status);

    Optional<EmailMessage> findByEmailInfo_EmailAndSubject(String email, ApiKeyMailSubjects subject);

    default EmailMessage getByEmailInfo_EmailAndSubject(String email, ApiKeyMailSubjects subject) {
        return findByEmailInfo_EmailAndSubject(email, subject)
                .orElseThrow(() -> new EmailMessageNotFoundException("Email message for " + email + " + wasn't found"));
    }

}
