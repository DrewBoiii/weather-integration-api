package dev.drewboiii.weatherintegrationapi.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfoDto {

    private String apiKey;

    private String requestUrl;

    private String country;

    private String method;

    private LocalDateTime createdAt;

}
