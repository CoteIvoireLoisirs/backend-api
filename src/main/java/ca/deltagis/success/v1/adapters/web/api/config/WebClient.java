/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-24 11:09:38
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-24 11:14:34
 */
package ca.deltagis.success.v1.adapters.web.api.config;

import java.util.ArrayList;
import java.util.List;


public class WebClient {
    public static final boolean isProduction = false;
    public static final boolean isTest = false;

    private static final String testUrl = "http://web.success.omcg.cloudns.cl";
    private static final String productionUrl = "https://api.success.ca";

    private static final List<String> localUrls = new ArrayList<>();

    public static List<String> getLocalUrls() {
        localUrls.add("http://localhost:3000");
        localUrls.add("http://localhost:4200");
        localUrls.add("http://localhost:4201");
        return localUrls;
    }

    public static List<String> url() {
        if (isProduction) {
            return List.of(productionUrl);
        }

        if (isTest) {
            return List.of(testUrl);
        }

        return getLocalUrls();
    }
}
