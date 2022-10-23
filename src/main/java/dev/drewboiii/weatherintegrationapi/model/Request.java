package dev.drewboiii.weatherintegrationapi.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Builder
@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Request extends AbstractDomainModel {

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
