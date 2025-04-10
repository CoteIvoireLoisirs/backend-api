
package ca.deltagis.success.v1.application.ports;

import ca.deltagis.success.v1.domain.core.models.email.EmailDetails;

/**
 * Interface for sending emails. This represents the port in a hexagonal architecture.
 * It defines the contract that any email service implementation must follow.
 *
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-24 11:58:36
 * @Description: Provides methods for sending plain text and HTML emails.
 */
public interface IEmailService {
    /**
     * Sends a simple text email.
     *
     * @param emailDetails The details of the email to be sent.
     * @return True if the email was sent successfully, false otherwise.
     */
    boolean sendSimpleEmail(EmailDetails emailDetails);

    /**
     * Sends an HTML formatted email.
     *
     * @param emailDetails The details of the email to be sent.
     * @return True if the email was sent successfully, false otherwise.
     */
    boolean sendHtmlEmail(EmailDetails emailDetails);
}
