package com.erastedev.ciexplore.v1.domain.ports.in.language;

import com.erastedev.ciexplore.v1.domain.models.language.LangCodeEnum;
import com.erastedev.ciexplore.v1.domain.models.language.Language;

import java.util.List;

public interface ISuccessLangService {

    /**
     * Retrieves the current language set for the application.
     *
     * @return the current language or the default language if not set.
     */
    Language getUserLanguage();

    /**
     * Sets the current language for the application.
     *
     * @param language the new current language.
     */
    void setUserLanguage(Language language);

    /**
     * Retrieves the default language code.
     *
     * @return the default language code.
     */
    LangCodeEnum getDefaultLanguageCode();

    /**
     * Retrieves the default language for the application.
     *
     * @return the default language or null if no default language is configured.
     */
    Language getDefaultLanguage();

    /**
     * Retrieves all languages.
     *
     * @return a list of all languages.
     */
    List<Language> all();
}
