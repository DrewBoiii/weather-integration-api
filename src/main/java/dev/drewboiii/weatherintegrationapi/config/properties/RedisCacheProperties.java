package dev.drewboiii.weatherintegrationapi.config.properties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Data
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "application.cache.redis")
public class RedisCacheProperties {

    @Valid
    List<CacheDefinitions> cacheDefinitions = new ArrayList<>();

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CacheDefinitions {

        /**
         * Cache name
         */
        @NotBlank
        String name;

        /**
         * Cache time-to-live value
         */
        @NotNull
        Duration ttl;
    }

}
