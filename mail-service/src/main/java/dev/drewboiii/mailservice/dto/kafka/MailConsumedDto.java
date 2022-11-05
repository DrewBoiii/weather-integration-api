package dev.drewboiii.mailservice.dto.kafka;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailConsumedDto {

    String emailTo;
    String subject;
    String payload;

}
