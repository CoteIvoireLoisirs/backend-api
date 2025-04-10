package com.erastedev.ciexplore.v1.application.services.language;

import com.erastedev.ciexplore.v1.application.services.user.UserAuthServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.UserServiceImpl;
import com.erastedev.ciexplore.v1.domain.entities.user.User;
import com.erastedev.ciexplore.v1.domain.models.language.LangCodeEnum;
import com.erastedev.ciexplore.v1.domain.models.language.Language;
import com.erastedev.ciexplore.v1.domain.ports.in.language.ISuccessLangService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuccessLangServiceImpl implements ISuccessLangService {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    UserAuthServiceImpl userAuthService;

    Logger logger = LoggerFactory.getLogger(SuccessLangServiceImpl.class);

    @Override
    public Language getUserLanguage() {
        User user = userAuthService.getCurrentLoggedUser();
        return all().stream().filter(l -> l.getCode().equals(user.getLanguage())).findFirst().orElse(getDefaultLanguage());
    }

    @Override
    public void setUserLanguage(Language language) {
        try {
            User user = userAuthService.getCurrentLoggedUser();
            user.setLanguage(language.getCode());
            userService.save(user);
        } catch (Exception e) {
            logger.error("Error setting user language", e);
            e.printStackTrace();
        }
    }

    @Override
    public LangCodeEnum getDefaultLanguageCode() {
        return LangCodeEnum.EN;
    }

    @Override
    public Language getDefaultLanguage() {
        return all().stream().filter(l -> l.getCode().equals(getDefaultLanguageCode())).findFirst().orElse(getDefaultLanguage());
    }

    @Override
    public List<Language> all() {
        return List.of(
                new Language(LangCodeEnum.EN, "English"),
                new Language(LangCodeEnum.FR, "French"),
                new Language(LangCodeEnum.ES, "Spanish")
        );
    }
}
