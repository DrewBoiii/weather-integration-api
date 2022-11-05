package dev.drewboiii.weatherintegrationapi.dto.request.kafka;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailDtoReply {

    String email;
    Boolean isDelivered;

}
