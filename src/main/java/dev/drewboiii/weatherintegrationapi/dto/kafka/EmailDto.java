package dev.drewboiii.weatherintegrationapi.dto.kafka;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class EmailDto {

    private String emailTo;
    private String subject;
    private String payload;

}
