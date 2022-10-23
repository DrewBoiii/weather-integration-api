package dev.drewboiii.weatherintegrationapi.aspect;

import dev.drewboiii.weatherintegrationapi.exception.TooManyRequestsToApiException;
import dev.drewboiii.weatherintegrationapi.service.CounterService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LimitHttpRequestsCountAspect {

    @Value("${application.security.max-requests-to-api}")
    Integer maxRequestsToApi;

    private final CounterService counterService;

    @SneakyThrows
    @Around("@annotation(LimitHttpRequestsPerApiKey)")
    public Object requestCount(ProceedingJoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        String shortMethodSignature = point.getSignature().toShortString();

        String cacheKey = buildCacheKey(shortMethodSignature);

        int counter = counterService.get(cacheKey);

        if (counter > maxRequestsToApi) {
            throw new TooManyRequestsToApiException("Too many requests to API Method: " + methodName);
        }

        counterService.increment(cacheKey, counter);

        return point.proceed();
    }

    private String buildCacheKey(String shortMethodSignature) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String apiKey = (String) authentication.getPrincipal();

        return DigestUtils.md5DigestAsHex((shortMethodSignature + apiKey).getBytes());
    }

}
