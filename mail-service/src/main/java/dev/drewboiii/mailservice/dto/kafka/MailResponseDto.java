package dev.drewboiii.mailservice.dto.kafka;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailResponseDto {

    String email;
    String subject;
    String payload;
    Boolean isDelivered;
    String error;

}
