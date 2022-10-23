package dev.drewboiii.weatherintegrationapi.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@SuppressWarnings("UnusedReturnValue")
public class CounterService {

    private static final String COUNTER_CACHE_NAME = "counter";

    @Cacheable(value = COUNTER_CACHE_NAME, key = "#counterName")
    public int get(@NonNull String counterName) {
        log.info("Getting cache counter: {}", counterName);
        return 1;
    }

    @CachePut(value = COUNTER_CACHE_NAME, key = "#counterName")
    public int increment(@NonNull String counterName, int value) {
        log.info("Incrementing cache counter: {}, value: {}", counterName, ++value);
        return value;
    }

    @CacheEvict(value = COUNTER_CACHE_NAME, key = "#counterName")
    public void reset(@NonNull String counterName) {
        log.info("Flushing cache counter: {}", counterName);
    }

}
