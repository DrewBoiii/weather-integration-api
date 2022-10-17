package dev.drewboiii.weatherintegrationapi.service;

import lombok.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CounterService {

    private static final String COUNTER_CACHE_NAME = "counter";

    @Cacheable(value = COUNTER_CACHE_NAME, key = "#counterName")
    public int get(@NonNull String counterName) {
        return 1;
    }

    @CachePut(value = COUNTER_CACHE_NAME, key = "#counterName")
    public int incrementAndGet(@NonNull String counterName, int value) {
        return ++value;
    }

    @CacheEvict(value = COUNTER_CACHE_NAME, key = "#counterName")
    public void reset(@NonNull String counterName) {

    }

}
