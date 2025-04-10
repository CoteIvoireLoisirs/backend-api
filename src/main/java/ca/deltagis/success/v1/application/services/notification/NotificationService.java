package ca.deltagis.success.v1.application.services.notification;


import ca.deltagis.success.v1.application.services.language.SuccessLangServiceImpl;
import ca.deltagis.success.v1.domain.core.models.language.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.deltagis.success.v1.application.ports.IEmailService;
import ca.deltagis.success.v1.application.services.notification.message.GuestEmail;
import ca.deltagis.success.v1.application.services.notification.message.RecoveryCodeEmail;
import ca.deltagis.success.v1.application.services.notification.message.RegisterConfirmationEmailMessage;
import ca.deltagis.success.v1.domain.core.entities.user.User;
import ca.deltagis.success.v1.domain.core.models.email.EmailDetails;

/**
 * Example service demonstrating how to use the EmailService.
 *
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-24 11:58:36
 * @Description: Uses the EmailService to send an email.
 */
@Service
public class NotificationService {

    private final IEmailService emailService;

    @Autowired
    public NotificationService(IEmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    private SuccessLangServiceImpl langService;

    /**
     * Sends an invitation email to the user with the given recipient email address.
     * The email contains a link to create a user account.
     *
     * @param recipient the recipient's email address
     * @param token     the unique token associated with the user invitation
     * @param url       the base URL for the signup page
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendInvitationEmail(String recipient, String token, String url) {
        try {
            Language language = langService.getUserLanguage();

            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(recipient);
            emailDetails.setSubject(GuestEmail.SUBJECT(language.getCode()));
            emailDetails.setBody(GuestEmail.MESSAGE(token, url, language.getCode()));

            return emailService.sendHtmlEmail(emailDetails);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sends an email to the user with the given recipient email address
     * containing a recovery code that can be used to reset the user's password.
     *
     * @param recipient the recipient's email address
     * @param code      the recovery code to send
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendRecoveryCode(String recipient, String code) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(recipient);
        emailDetails.setSubject(RecoveryCodeEmail.SUBJECT);
        emailDetails.setBody(RecoveryCodeEmail.MESSAGE(code));
        return emailService.sendHtmlEmail(emailDetails);

    }

    /**
     * Sends a registration confirmation email to the specified recipient.
     * <p>
     * The email includes a subject and body content confirming the user's
     * successful registration. The email is sent using the email service's
     * simple email method.
     *
     * @param recipient the recipient's email address
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendRegisterConfirmationEmailMessage(User recipient) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(recipient.getEmail());
        emailDetails.setSubject(RegisterConfirmationEmailMessage.SUBJECT);
        emailDetails.setBody(RegisterConfirmationEmailMessage.MESSAGE(recipient.getDisplayName(), recipient.getUsername()));
        return emailService.sendHtmlEmail(emailDetails);
    }
}
