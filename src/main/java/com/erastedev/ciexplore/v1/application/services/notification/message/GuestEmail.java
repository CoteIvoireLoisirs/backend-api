
package com.erastedev.ciexplore.v1.application.services.notification.message;

import com.erastedev.ciexplore.v1.domain.models.language.LangCodeEnum;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-24 12:25:38
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:46
 * @FilePath: src/main/java/ca/deltagis/success/v1/application/services/notification/message/GuestEmail.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
public class GuestEmail {
    public static String SUBJECT(LangCodeEnum langCode) {
        switch (langCode) {
            case FR:
                return "[SUCCESS] Invitation to create user account";
            default:
                return "[SUCCESS] Invitation to create user account";
        }
    }

    /**
     * Generates an invitation email message with a link to create a user account.
     *
     * @param token the unique token associated with the user invitation
     * @param url   the base URL for the signup page
     * @return a formatted HTML message containing the invitation details and a signup link
     */
    public static String MESSAGE(String token, String url, LangCodeEnum langCode) {
        String link = url + "/#/auth/signup/" + token;
        //String link = url + "/#/signup/" + token;

        if (!link.contains("https") && !link.contains("http")) {
            link = "https://" + link;
        }
        String message = "";

        // Contenu à insérer dans le template existant
        switch (langCode) {
            case FR:
                message = "<p style='font-size: 16px; color: #555;'>This is an invitation to create a SUCCESS user account.</p>";
                message += "<p style='font-size: 16px; color: #555;'>Please follow the link below to accept the invitation and create your account:</p>";
                message += "<p><a href='" + link + "' style='background-color: #007BFF; color: #ffffff; padding: 10px 15px; text-decoration: none; border-radius: 5px;' target='_blank'>Create Your Account</a></p>";
                message += "<p style='font-size: 14px; color: #555;'>If the button above does not work, copy and paste the following link into your browser:</p>";
                message += "<p><a href='" + link + "' style='word-wrap: break-word; color: #007BFF;' target='_blank'>" + link + "</a></p>";
                message += "<p style='font-size: 14px; color: #999;'>You must respond to this invitation within 02 days. If you take no action, the invitation will expire at that time.</p>";
                break;

            default:
                message = "<p style='font-size: 16px; color: #555;'>This is an invitation to create a SUCCESS user account.</p>";
                message += "<p style='font-size: 16px; color: #555;'>Please follow the link below to accept the invitation and create your account:</p>";
                message += "<p><a href='" + link + "' style='background-color: #007BFF; color: #ffffff; padding: 10px 15px; text-decoration: none; border-radius: 5px;' target='_blank'>Create Your Account</a></p>";
                message += "<p style='font-size: 14px; color: #555;'>If the button above does not work, copy and paste the following link into your browser:</p>";
                message += "<p><a href='" + link + "' style='word-wrap: break-word; color: #007BFF;' target='_blank'>" + link + "</a></p>";
                message += "<p style='font-size: 14px; color: #999;'>You must respond to this invitation within 02 days. If you take no action, the invitation will expire at that time.</p>";
                break;
        }

        return message;
    }
}
    