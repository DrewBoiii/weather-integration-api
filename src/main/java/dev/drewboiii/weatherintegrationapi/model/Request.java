package dev.drewboiii.weatherintegrationapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = ApiKey.class)
    private ApiKey apiKey;

    @Column(name = "url")
    private String requestUrl;

    @Column
    private String country;

    @Column
    private String method;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    void init() {
        this.createdAt = LocalDateTime.now();
    }

}
