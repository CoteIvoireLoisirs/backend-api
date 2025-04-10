package com.erastedev.ciexplore.v1.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for the password encoder used by the application.
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * The password encoder used by the application. This is a bean that can be autowired into any component.
     *
     * @return the password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}