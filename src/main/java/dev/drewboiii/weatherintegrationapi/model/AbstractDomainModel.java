package dev.drewboiii.weatherintegrationapi.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;


@Getter
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class AbstractDomainModel {

    @Id
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @GeneratedValue(generator = "UUID")
    @Column(name = "uuid", unique = true, updatable = false, nullable = false)
    UUID uuid;

    @Version
    @Column(name = "version")
    Integer version;

}
