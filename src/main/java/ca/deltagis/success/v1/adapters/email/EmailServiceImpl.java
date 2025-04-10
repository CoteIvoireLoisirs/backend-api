package ca.deltagis.success.v1.adapters.email;


import ca.deltagis.success.v1.application.ports.IEmailService;
import ca.deltagis.success.v1.domain.core.models.email.EmailDetails;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


/**
 * Implementation of the EmailService interface.
 * This class handles the actual sending of emails using JavaMailSender.
 *
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-24 11:58:36
 * @Description: Sends plain text and HTML emails using SMTP configurations.
 */
@Service
public class EmailServiceImpl implements IEmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a simple text email to the specified recipient.
     * <p>
     * This method creates a MIME message with the given email details,
     * including recipient address, subject, and plain text body content.
     * It uses the JavaMailSender to send the email. If an error occurs
     * during the process, it logs the error and returns false.
     *
     * @param emailDetails Contains the recipient, subject, and body of the email.
     * @return true if the email was sent successfully, false otherwise.
     */
    @Override
    public boolean sendSimpleEmail(EmailDetails emailDetails) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(senderEmail);
            helper.setTo(emailDetails.getRecipient());
            helper.setSubject(emailDetails.getSubject());
            helper.setText(emailDetails.getBody(), false);

            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error sending {}", e.getMessage());
            logger.info("Error sending {}", emailDetails.getRecipient());
        }
        return false;
    }

    /**
     * Sends an HTML formatted email to the specified recipient.
     * <p>
     * This method creates a MIME message with the given email details,
     * including recipient address, subject, and HTML body content. It uses
     * the JavaMailSender to send the email.
     *
     * @param emailDetails Contains the recipient, subject, and HTML body of the email.
     * @return true if the email was sent successfully, false otherwise.
     */
    @Override
    public boolean sendHtmlEmail(EmailDetails emailDetails) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            Context context = new Context();
            context.setVariable("message", emailDetails.getBody());
            String htmlContent = templateEngine.process(EmailTemplate.DEFAULT.getValue(), context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(senderEmail);
            helper.setTo(emailDetails.getRecipient());
            helper.setSubject(emailDetails.getSubject());
            if(emailDetails.getCc() != null) {
                helper.setCc(emailDetails.getCc().toArray(String[]::new));
            }
            if(emailDetails.getBcc() != null) {
                helper.setBcc(emailDetails.getBcc().toArray(String[]::new));
            }
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("Error sending getMessage:{}, getRecipient:{}", e.getMessage(), emailDetails.getRecipient());
            e.printStackTrace();
        }
        return false;
    }
}