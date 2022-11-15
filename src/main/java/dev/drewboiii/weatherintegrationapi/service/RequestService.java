package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.dto.response.RequestInfoDto;
import dev.drewboiii.weatherintegrationapi.model.Request;
import dev.drewboiii.weatherintegrationapi.persistence.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    public void deleteRequestsExpiredBy(Period interval) {
        LocalDateTime dateBefore = ZonedDateTime.now().minus(interval).toLocalDateTime();
        requestRepository.deleteRequestsExpiredBy(dateBefore);
    }


    public Page<RequestInfoDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return requestRepository.findAll(pageable)
                .map(this::toDto);
    }

    private RequestInfoDto toDto(Request request) {
        return RequestInfoDto.builder()
                .requestUrl(request.getRequestUrl())
                .country(request.getCountry())
                .method(request.getMethod())
                .createdAt(request.getCreatedAt())
                .apiKey(request.getApiKey().getContent())
                .build();
    }
}
