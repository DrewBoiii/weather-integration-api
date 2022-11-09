package dev.drewboiii.weatherintegrationapi.dto.kafka;

import lombok.*;

@Data
@NoArgsConstructor
public class EmailReplyConsumedDto {

    String email;
    String subject;
    String payload;
    Boolean isDelivered;
    String error;

}
