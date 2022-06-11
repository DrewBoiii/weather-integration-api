package dev.drewboiii.weatherintegrationapi.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ApiKeyRefreshRequestDto {

    @NotBlank
    private String email;

    @NotBlank
    private String key;

}
