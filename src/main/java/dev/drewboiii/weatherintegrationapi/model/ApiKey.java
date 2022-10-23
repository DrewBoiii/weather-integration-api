package dev.drewboiii.weatherintegrationapi.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Builder
@Entity
@Table(name = "api_key")
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ApiKey extends AbstractDomainModel {

    @Column(unique = true)
    private String content;

    @Column(unique = true)
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, mappedBy = "apiKey")
    private List<Request> requests;

    @PrePersist
    void init() {
        this.createdAt = LocalDateTime.now();
        this.validUntil = LocalDateTime.now().plusDays(7L);
    }

}
