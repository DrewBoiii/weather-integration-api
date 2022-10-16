package dev.drewboiii.weatherintegrationapi.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Before("@annotation(getMapping)")
    public void logApiGetMethods(JoinPoint joinPoint, GetMapping getMapping) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = methodSignature.getParameterNames();

        Map<String, Object> nameValueArgMap = buildArgs(parameterNames, args);

        log.info("HTTP GET Request. Path: {}, Method name: {}, Args: {}", getMapping.value()[0], methodName, nameValueArgMap);
    }

    @Before("execution(* dev.drewboiii.weatherintegrationapi.service.ApiKey*.*(*)) " +
            "&& !execution(* dev.drewboiii.weatherintegrationapi.service.ApiKeyGeneratorService.*(*))")
    public void logApiKeyService(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = methodSignature.getParameterNames();

        Map<String, Object> nameValueArgMap = buildArgs(parameterNames, args);

        log.info("API Key {} with args: {}", methodName, nameValueArgMap);
    }

    private Map<String, Object> buildArgs(String[] parameterNames, Object[] args) {
        return IntStream.range(0, parameterNames.length)
                .collect(HashMap::new, (map, index) -> map.put(parameterNames[index], args[index]), Map::putAll);
    }

}
