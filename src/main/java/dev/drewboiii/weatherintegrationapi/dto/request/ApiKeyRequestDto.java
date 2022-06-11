package dev.drewboiii.weatherintegrationapi.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ApiKeyRequestDto {

    @NotBlank
    private String email;

}
