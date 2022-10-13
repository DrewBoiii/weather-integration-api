package dev.drewboiii.weatherintegrationapi.service;

import dev.drewboiii.weatherintegrationapi.persistence.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    public void deleteRequestsExpiredBy(Period interval) {
        LocalDateTime dateBefore = ZonedDateTime.now().minus(interval).toLocalDateTime();
        requestRepository.deleteRequestsExpiredBy(dateBefore);
    }

}
