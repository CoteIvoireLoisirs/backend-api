package com.erastedev.ciexplore.v1.infrastructure.config.language;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceConfig {
    /**
     * Provides a {@link MessageSource} for translating messages.
     *
     * <p>This bean returns a {@link ResourceBundleMessageSource} that loads
     * translation messages from resource bundles named "messages" (i.e.
     * "messages.properties", "messages_en.properties", etc.). The default
     * encoding is UTF-8.
     *
     * @return a {@link MessageSource}
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages"); // Load messages from resource bundles
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
