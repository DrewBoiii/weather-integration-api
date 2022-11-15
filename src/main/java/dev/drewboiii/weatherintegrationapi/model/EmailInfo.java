package dev.drewboiii.weatherintegrationapi.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Table(indexes = @Index(name = "email", columnList = "email"))
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailInfo extends AbstractDomainModel {

    @Column(unique = true)
    private String email;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "emailInfo")
    private List<EmailMessage> emailMessages;

}
