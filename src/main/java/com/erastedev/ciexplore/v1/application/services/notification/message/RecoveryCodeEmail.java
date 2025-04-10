/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-25 02:57:27
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-25 02:57:46
 */
package com.erastedev.ciexplore.v1.application.services.notification.message;

public class RecoveryCodeEmail {
    public static String SUBJECT = "[SUCCESS] Recovery code";

    /**
     * Generates an invitation email message with a link to create a user account.
     *
     * @param code the unique token associated with the user invitation
     * @return a formatted HTML message containing the invitation details and a signup link
     */
    public static String MESSAGE(String code) {
        String message = "<p style='font-size: 16px; color: #555;'>This is the recovery code for your account.</p>";
        message += "<p style='font-size: 16px; color: #555;'>Your recovery code is:</p>";
        message += "<p style='font-size: 16px; color: #555;'>" + code + "</p>";


        return message;
    }
}
