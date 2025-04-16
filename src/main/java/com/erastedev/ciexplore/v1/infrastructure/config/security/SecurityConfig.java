package com.erastedev.ciexplore.v1.infrastructure.config.security;

import com.erastedev.ciexplore.v1.adapters.web.api.config.WebClient;
import com.erastedev.ciexplore.v1.adapters.web.api.endpoints.Endpoint;
import com.erastedev.ciexplore.v1.adapters.web.http.CustomAccessDeniedHandler;
import com.erastedev.ciexplore.v1.application.services.user.auth.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.userDetailsService = customUserDetailsService;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;

    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        return authenticationManagerBuilder.build();
    }

    // @Bean
    // public WebMvcConfigurer corsConfigurer() {
    //     return new WebMvcConfigurer() {
    //         @Override
    //         public void addCorsMappings(CorsRegistry registry) {
    //             registry.addMapping("/**")
    //                     .allowedOrigins(WebClient.url())
    //                     .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    //                     .allowedHeaders("*")
    //                     .allowCredentials(true);
    //         }
    //     };
    // }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                Endpoint.REGISTER_FIRST_USER_ENDPOINT,
                                Endpoint.LOGIN_USER_ENDPOINT,
                                Endpoint.REGISTER_USER_ENDPOINT,
                                Endpoint.VERIFY_RECOVERY_CODE_ENDPOINT,
                                Endpoint.RESET_PASSWORD_ENDPOINT,
                                Endpoint.FORGET_PASSWORD_ENDPOINT,

                                // TODO : remove, must be authorized by JWT
                                Endpoint.INVITE_USER_ENDPOINT,
                                Endpoint.REFRESH_TOKEN_ENDPOINT,
                                Endpoint.PUBLIC_WORKSPACE_ENDPOINT,
                                Endpoint.PUBLIC_ASSET_ENDPOINT + "/**",
                                Endpoint.I18N + "/**",
                                // TODO : remove, must be authorized by JWT
                                Endpoint.SWAGGER_UI,
                                Endpoint.SWAGGER_UI_PATH,
                                Endpoint.SWAGGER_V3_API_DOCS,
                                Endpoint.LOGOUT_USER_ENDPOINT,

                                "/uploads/**"
                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(WebClient.url());
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}


