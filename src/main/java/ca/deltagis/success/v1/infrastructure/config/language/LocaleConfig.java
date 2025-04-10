package ca.deltagis.success.v1.infrastructure.config.language;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
public class LocaleConfig {
    /**
     * Configures the locale resolver for the application. The locale resolver
     * is responsible for determining the locale to use for localizing the
     * application. In this case, the locale is determined based on the
     * Accept-Language header sent by the client. The default locale is
     * English and the supported locales are English and French.
     *
     * @return the locale resolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH); // Set the default locale
        localeResolver.setSupportedLocales(List.of(Locale.ENGLISH, Locale.FRENCH)); // Set the supported locales
        return localeResolver;
    }
}
