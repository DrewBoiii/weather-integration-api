package dev.drewboiii.weatherintegrationapi.controller;

import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRefreshRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.response.ApiKeyResponseDto;
import dev.drewboiii.weatherintegrationapi.service.ApiKeyService;
import dev.drewboiii.weatherintegrationapi.service.ApiKeyFacade;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/key")
@AllArgsConstructor
public class ApiKeyController {

    private final ApiKeyFacade apiKeyFacade;
    private final ApiKeyService apiKeyService;

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiKeyResponseDto generateApiKey(@RequestBody @Valid ApiKeyRequestDto apiKeyRequestDto) {
        return apiKeyFacade.generateApiKey(apiKeyRequestDto);
    }

    @GetMapping("/{key}/details")
    public ApiKeyResponseDto getDetails(@PathVariable("key") String apiKey) {
        return apiKeyService.getDetails(apiKey);
    }

    @GetMapping("/{key}/details/inline")
    public String getDetailsInline(@PathVariable("key") String apiKey, @RequestParam String email) {
        return apiKeyService.getDetailsInline(apiKey, email);
    }

    @PatchMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ApiKeyResponseDto refreshApiKey(@RequestBody @Valid ApiKeyRefreshRequestDto apiKeyRefreshRequestDto) {
        return apiKeyFacade.refreshApiKey(apiKeyRefreshRequestDto);
    }

}
