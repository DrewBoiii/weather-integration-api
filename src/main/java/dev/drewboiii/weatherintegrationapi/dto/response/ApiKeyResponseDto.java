package dev.drewboiii.weatherintegrationapi.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiKeyResponseDto {

    private final String apiKey;
    private final String mail;
    private final LocalDateTime createdAt;
    private final LocalDateTime validUntil;
    private final Long validInDays;

}
