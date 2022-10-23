package dev.drewboiii.weatherintegrationapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.drewboiii.weatherintegrationapi.config.properties.RedisCacheProperties;
import lombok.NonNull;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.util.Map;

import static java.util.stream.Collectors.toMap;


@Configuration
@EnableCaching
@EnableConfigurationProperties(RedisCacheProperties.class)
public class CacheConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerCustomizer(@NonNull RedisCacheProperties redisCacheProperties,
                                                                          @NonNull ObjectMapper objectMapper) {

        return redisCacheManagerBuilder -> {
            RedisSerializationContext.SerializationPair<Object> serializer =
                    RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));

            Map<String, RedisCacheConfiguration> configurationMap = redisCacheProperties.getCacheDefinitions().stream()
                    .collect(
                            toMap(
                                    RedisCacheProperties.CacheDefinitions::getName,
                                    cacheDefinitions -> RedisCacheConfiguration
                                            .defaultCacheConfig(getClass().getClassLoader())
                                            .serializeValuesWith(serializer)
                                            .entryTtl(cacheDefinitions.getTtl())
                            )
                    );

            if (!configurationMap.isEmpty()) {
                redisCacheManagerBuilder.withInitialCacheConfigurations(configurationMap);
            }
        };
    }

}
