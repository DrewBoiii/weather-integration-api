package dev.drewboiii.weatherintegrationapi.aspect;

import dev.drewboiii.weatherintegrationapi.exception.TooManyRequestsToApiException;
import dev.drewboiii.weatherintegrationapi.service.CounterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class LimitHttpRequestsCountAspect {

    CounterService counterService;

    @SneakyThrows
    @Around("@annotation(LimitHttpRequestsPerApiKey)")
    public Object requestCount(ProceedingJoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();

        int maxRequestCount = 5; // TODO: 10/17/2022 property

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String apiKey = (String) authentication.getPrincipal();

        String shortMethodSignature = point.getSignature().toShortString();
        String cacheKey = DigestUtils.md5DigestAsHex((shortMethodSignature + apiKey).getBytes());

        int counter = counterService.get(cacheKey);

        if (counter > maxRequestCount) {
            log.warn("Too many requests to API Method {}", method.getName());
            throw new TooManyRequestsToApiException("Too many requests to API Method: " + method.getName());
        }

        int incrementedCounter = counterService.incrementAndGet(cacheKey, counter);
        log.info("Request counter value after increment for API Key {} is {}", apiKey, incrementedCounter);

        return point.proceed();
    }

}
