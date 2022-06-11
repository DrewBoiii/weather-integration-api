package dev.drewboiii.weatherintegrationapi.controller;

import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.response.ApiKeyResponseDto;
import dev.drewboiii.weatherintegrationapi.service.ApiKeyService;
import dev.drewboiii.weatherintegrationapi.service.WeatherFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/key")
@AllArgsConstructor
public class ApiKeyController {

    private final WeatherFacade weatherFacade;
    private final ApiKeyService apiKeyService;

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiKeyResponseDto generateApiKey(@RequestBody @Valid ApiKeyRequestDto apiKeyRequestDto) {
        return weatherFacade.generateApiKey(apiKeyRequestDto);
    }

    @GetMapping("/{key}/details")
    public ApiKeyResponseDto getDetails(@PathVariable("key") String apiKey) {
        return apiKeyService.getDetails(apiKey);
    }

    @GetMapping("/{key}/details/inline")
    public String getDetailsInline(@PathVariable("key") String apiKey, @RequestParam String email) {
        return apiKeyService.getDetailsInline(apiKey, email);
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public ApiKeyResponseDto refreshApiKey(@RequestBody @Valid ApiKeyRequestDto apiKeyRequestDto) {
//        return apiKeyService.refresh();
//    }

}
