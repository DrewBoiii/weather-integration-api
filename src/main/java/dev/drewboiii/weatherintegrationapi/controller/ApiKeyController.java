package dev.drewboiii.weatherintegrationapi.controller;

import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRefreshRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.request.ApiKeyRequestDto;
import dev.drewboiii.weatherintegrationapi.dto.response.ApiKeyResponseDto;
import dev.drewboiii.weatherintegrationapi.service.ApiKeyFacade;
import dev.drewboiii.weatherintegrationapi.service.ApiKeyService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
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
    public EntityModel<ApiKeyResponseDto> generate(@RequestBody @Valid ApiKeyRequestDto apiKeyRequestDto) {
        return apiKeyFacade.generate(apiKeyRequestDto);
    }

    @GetMapping("/{key}/details")
    public EntityModel<ApiKeyResponseDto> details(@PathVariable("key") String apiKey) {
        return apiKeyFacade.details(apiKey);
    }

    @GetMapping("/{key}/details/inline")
    public String detailsInline(@PathVariable("key") String apiKey, @RequestParam String email) {
        return apiKeyService.getDetailsInline(apiKey, email);
    }

    @PatchMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<ApiKeyResponseDto> refresh(@RequestBody @Valid ApiKeyRefreshRequestDto apiKeyRefreshRequestDto) {
        return apiKeyFacade.refresh(apiKeyRefreshRequestDto);
    }

}
