package dev.drewboiii.weatherintegrationapi.model;

import dev.drewboiii.weatherintegrationapi.config.meta.MailDeliveringStatuses;
import lombok.*;

import javax.persistence.*;

@Table(indexes = @Index(name = "email", columnList = "email"))
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage extends AbstractDomainModel {

    @Column(unique = true)
    private String email;

    @Column
    @Enumerated(EnumType.STRING)
    private MailDeliveringStatuses status;

    @PrePersist
    private void init() {
        this.status = MailDeliveringStatuses.NONE;
    }

}
