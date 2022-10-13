package dev.drewboiii.weatherintegrationapi.persistence;

import dev.drewboiii.weatherintegrationapi.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID>, JpaSpecificationExecutor<Request> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Request r WHERE r.createdAt < :date AND r.apiKey.uuid IS NULL")
    void deleteRequestsExpiredBy(LocalDateTime date);

}
