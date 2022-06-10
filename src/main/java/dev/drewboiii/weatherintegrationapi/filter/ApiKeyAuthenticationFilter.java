package dev.drewboiii.weatherintegrationapi.filter;

import dev.drewboiii.weatherintegrationapi.model.WeatherAuthApiKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
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
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String API_KEY_AUTHORIZATION_HEADER = "X-Weather-API-Key";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String apiKey = request.getHeader(API_KEY_AUTHORIZATION_HEADER);

        if (apiKey == null || apiKey.isBlank()) {
            log.error("Request with a blank API Key.");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("API Key is empty.");
            return;
        }

        if (!apiKey.equals("hardcoded-api-key")) {
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
