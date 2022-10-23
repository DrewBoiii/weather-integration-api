package dev.drewboiii.weatherintegrationapi.schedule;

import dev.drewboiii.weatherintegrationapi.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Period;

@Component
@RequiredArgsConstructor
public class TableCleanerSchedule {

    @Value("${application.scheduler.clean-table.request.interval:P1M}")
    private Period interval;

    private final RequestService requestService;

    private final ApplicationContext applicationContext;
    private TableCleanerSchedule self;

    @Scheduled(cron = "${application.scheduler.clean-table.request.cron:-}")
    public void cleanRequestTable() {
        requestService.deleteRequestsExpiredBy(interval);
    }

    @PostConstruct
    private void init() {
        this.self = applicationContext.getBean(TableCleanerSchedule.class);
    }

}
