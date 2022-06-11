package dev.drewboiii.weatherintegrationapi.filter;

import dev.drewboiii.weatherintegrationapi.model.WeatherAuthApiKey;
import dev.drewboiii.weatherintegrationapi.persistence.ApiKeyRepository;
import dev.drewboiii.weatherintegrationapi.persistence.projection.ApiKeyShortDetailsProjection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String API_KEY_AUTHORIZATION_HEADER = "X-Weather-API-Key";

    private final ApiKeyRepository apiKeyRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String apiKey = request.getHeader(API_KEY_AUTHORIZATION_HEADER);

        if (apiKey == null || apiKey.isBlank()) {
            log.error("Request with a blank API Key.");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("API Key is empty.");
            return;
        }

        ApiKeyShortDetailsProjection apiKeyDetails = apiKeyRepository.getByContent(apiKey);

        if (apiKeyDetails == null) {
            log.error("Request with an invalid API Key.");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("Invalid API Key.");
            return;
        }

        WeatherAuthApiKey apiToken = new WeatherAuthApiKey(apiKey, AuthorityUtils.NO_AUTHORITIES);
        apiToken.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(apiToken);

        filterChain.doFilter(request, response);
    }

}
