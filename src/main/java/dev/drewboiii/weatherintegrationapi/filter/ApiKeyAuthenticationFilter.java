package dev.drewboiii.weatherintegrationapi.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.drewboiii.weatherintegrationapi.model.WeatherAuthApiKey;
import dev.drewboiii.weatherintegrationapi.persistence.ApiKeyRepository;
import dev.drewboiii.weatherintegrationapi.persistence.projection.ApiKeyShortDetailsProjection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@AllArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String API_KEY_AUTHORIZATION_HEADER = "X-Weather-API-Key";

    private final ApiKeyRepository apiKeyRepository;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String apiKey = request.getHeader(API_KEY_AUTHORIZATION_HEADER);

        Map<String, Object> errorDetails = new HashMap<>();

        if (apiKey == null || apiKey.isBlank()) {
            log.error("Request with a blank API Key");
            errorDetails.put("error", "API Key is empty");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            mapper.writeValue(response.getWriter(), errorDetails);
            return;
        }

        ApiKeyShortDetailsProjection apiKeyDetails = apiKeyRepository.getByContent(apiKey);

        if (apiKeyDetails == null) {
            log.error("Request with an invalid API Key");
            errorDetails.put("error", "Invalid API Key");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            mapper.writeValue(response.getWriter(), errorDetails);
            return;
        }

        LocalDateTime validUntil = apiKeyDetails.getValidUntil();
        if (validUntil.isBefore(LocalDateTime.now())) {
            log.error("Request with an expired API Key");
            errorDetails.put("error", "Expired API Key");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            mapper.writeValue(response.getWriter(), errorDetails);
            return;
        }

        WeatherAuthApiKey apiToken = new WeatherAuthApiKey(apiKey, AuthorityUtils.NO_AUTHORITIES);
        apiToken.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(apiToken);

        filterChain.doFilter(request, response);
    }

}
