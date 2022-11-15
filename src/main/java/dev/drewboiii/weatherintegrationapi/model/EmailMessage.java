package dev.drewboiii.weatherintegrationapi.model;


import dev.drewboiii.weatherintegrationapi.config.meta.ApiKeyMailSubjects;
import dev.drewboiii.weatherintegrationapi.config.meta.MailDeliveringStatuses;
import lombok.*;

import javax.persistence.*;

@Table
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage extends AbstractDomainModel {

    @Column
    @Enumerated(EnumType.STRING)
    private MailDeliveringStatuses status;

    @Column
    private String payload;

    @Column
    @Enumerated(EnumType.STRING)
    private ApiKeyMailSubjects subject;

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = EmailInfo.class)
    private EmailInfo emailInfo;

    @PrePersist
    private void init() {
        this.status = MailDeliveringStatuses.NONE;
    }

}
