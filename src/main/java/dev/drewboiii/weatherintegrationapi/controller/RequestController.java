package dev.drewboiii.weatherintegrationapi.controller;

import dev.drewboiii.weatherintegrationapi.dto.response.RequestInfoDto;
import dev.drewboiii.weatherintegrationapi.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/request")
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public Page<RequestInfoDto> getRequests(@RequestParam Integer page,
                                            @RequestParam Integer size) {
        return requestService.getAll(page, size);
    }

}
