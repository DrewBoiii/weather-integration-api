package dev.drewboiii.weatherintegrationapi.schedule;

import dev.drewboiii.weatherintegrationapi.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Period;

@Component
@RequiredArgsConstructor
public class TableCleanerSchedule {

    @Value("${schedule.clean-table.request.interval:P1M}")
    private Period interval;

    private final RequestService requestService;

    @Scheduled(cron = "${schedule.clean-table.request.cron:-}")
    public void cleanRequestTable() {
        requestService.deleteRequestsExpiredBy(interval);
    }

}
