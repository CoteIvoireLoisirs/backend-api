package com.erastedev.ciexplore.v1.application.utils;

import com.erastedev.ciexplore.v1.adapters.web.api.config.WebClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServerUtil {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Getter
    @Value("${file.storage.location}")
    private String assetFolder;

    @Getter
    @Value("${file.storage.resources}")
    private String resourceFolder;

    private static final String localUrl = "http://localhost:8083";
    private static final String testUrl = "http://192.168.1.100:8081";
    private static final String productionUrl = "https://success.web.delta-monitoring.com";

    /**
     * Return the current environment profile.
     *
     * @return the active profile
     */
    public String currentEnvironment() {
        return activeProfile;
    }

    /**
     * Returns the URL corresponding to the current environment.
     * If the environment is "test", it returns the test URL.
     * If the environment is "production", it returns the production URL.
     * Otherwise, it returns the local URL.
     *
     * @return the URL for the current environment
     */
    public String url() {
        String currentEnvironment = currentEnvironment();

        if (WebClient.isProduction) {
            return productionUrl;
        } else if (WebClient.isTest) {
            return testUrl;
        }
        return localUrl;

//        if (currentEnvironment.equals("test")) {
//            return testUrl;
//        }
//
//        if (currentEnvironment.equals("production")) {
//            return productionUrl;
//        }
//
//        return localUrl;
    }
}
