package dev.drewboiii.weatherintegrationapi.persistence;

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

@Repository
public interface EmailMessageRepository extends JpaRepository<EmailMessage, String> {

    Optional<EmailMessage> findByEmail(String email);

    default EmailMessage getByEmailOrThrow(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new EmailMessageNotFoundException("Email message was not found"));
    }

    @Modifying
    @Transactional
    @Query("UPDATE EmailMessage em SET em.status = :status WHERE em.email = :email")
    int updateStatus(@Param(value = "email") String email, @Param(value = "status") MailDeliveringStatuses status);

}
