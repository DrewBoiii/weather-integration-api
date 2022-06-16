package dev.drewboiii.weatherintegrationapi.filter;

import dev.drewboiii.weatherintegrationapi.model.ApiKey;
import dev.drewboiii.weatherintegrationapi.model.Request;
import dev.drewboiii.weatherintegrationapi.persistence.ApiKeyRepository;
import dev.drewboiii.weatherintegrationapi.persistence.RequestRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class LogRequestFilter extends OncePerRequestFilter {

    private static final String API_KEY_AUTHORIZATION_HEADER = "X-Weather-API-Key";

    private final RequestRepository requestRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String apiKey = request.getHeader(API_KEY_AUTHORIZATION_HEADER);

        ApiKey key = apiKeyRepository.findApiKeyByContent(apiKey).orElse(null);

        Request apiKeyRequest = Request.builder()
                .apiKey(key)
                .requestUrl(request.getRequestURI())
                .country(request.getLocale().getCountry())
                .method(request.getMethod())
                .build();

        requestRepository.save(apiKeyRequest);

        log.info("Request {} was sent.", apiKeyRequest.getUuid());

        filterChain.doFilter(request, response);
    }
}
