package dev.drewboiii.weatherintegrationapi.persistence;

import dev.drewboiii.weatherintegrationapi.exception.EmailMessageNotFoundException;
import dev.drewboiii.weatherintegrationapi.model.EmailInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface EmailInfoRepository extends JpaRepository<EmailInfo, String> {

    Optional<EmailInfo> findByEmail(String email);

    default EmailInfo getByEmailOrThrow(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new EmailMessageNotFoundException("Email info was not found"));
    }

    @Modifying
    @Transactional
    @Query("UPDATE EmailInfo ei SET ei.email = :new_email WHERE ei.email = :old_email")
    int updateEmail(@Param("old_email") String oldEmail, @Param("new_email") String newEmail);

}
