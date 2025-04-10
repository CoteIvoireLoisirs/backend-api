package ca.deltagis.success.v1.infrastructure.config.security;

import ca.deltagis.success.v1.adapters.web.api.ApiResponse;
import ca.deltagis.success.v1.adapters.web.api.service.ApiResponseService;
import ca.deltagis.success.v1.application.services.user.auth.JwtServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;
    private final ObjectMapper mapper;
    private final ApiResponseService apiResponseService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    @Autowired
    public JwtAuthorizationFilter(JwtServiceImpl jwtService, ObjectMapper mapper, ApiResponseService apiResponseService) {
        this.jwtService = jwtService;
        this.mapper = mapper;
        this.apiResponseService = apiResponseService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String accessToken = jwtService.resolveToken(request);
            if (accessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Claims claims = jwtService.extractAllClaims(accessToken);
            if (claims != null && !jwtService.isTokenExpired(accessToken)) {
                String email = claims.getSubject();
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                defaultResponse(response, "Invalid or expired token", HttpStatus.FORBIDDEN.value());
                return;
            }

        } catch (Exception e) {
            logger.error("Authentication error: {}", e.getMessage());
            defaultResponse(response, "Authentication Error", HttpStatus.FORBIDDEN.value(), e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void defaultResponse(HttpServletResponse response, String message, int status) throws IOException {
        ApiResponse<String> apiResponse = new ApiResponse<>(status, message, null, null, null, false);
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), apiResponse);
    }

    private void defaultResponse(HttpServletResponse response, String message, int status, String errorDetails) throws IOException {
        ApiResponse<String> apiResponse = new ApiResponse<>(status, message, null, errorDetails, null, false);
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), apiResponse);
    }
}
