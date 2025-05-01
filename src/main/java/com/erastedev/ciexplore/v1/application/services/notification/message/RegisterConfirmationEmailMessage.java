package com.erastedev.ciexplore.v1.application.services.notification.message;

public class RegisterConfirmationEmailMessage {

    public static final String SUBJECT = "REGISTRATION SUCCESSFUL";

    /**
     * Generates a personalized registration email confirmation with a link to connect to the application.
     *
     * @param fullName  the name of the user
     * @param userName the email of the user
     * @param verifyCode
     * @return a formatted HTML message containing the registration confirmation and a sign-in link
     */
    public static String MESSAGE(String fullName, String userName, String verifyCode) {
        return "<div style='font-family: Arial, sans-serif; color: #333;'>"
                + "<h2 style='color: #333;'>Hello " + fullName + ",</h2>"
                + "<p style='font-size: 16px; color: #555;'>We are pleased to inform you that your account has been successfully created.</p>"
                + "<p style='font-size: 16px; color: #555;'>You can now log in using the following details:</p>"
                + "<ul style='font-size: 16px; color: #555;'>"
                + "<li style='font-size: 16px; color: #555; list-style-type: none' ><strong> Username  : </strong> " + userName + "</li>"
                + "<li style='font-size: 16px; color: #555; list-style-type: none' ><strong> Verify code  : </strong> " + verifyCode + "</li>"
                + "</ul>"
                + "<br>"
                + "<p style='font-size: 16px; color: #555;'>Best regards,<br>"
                + "</div>";
    }
}
