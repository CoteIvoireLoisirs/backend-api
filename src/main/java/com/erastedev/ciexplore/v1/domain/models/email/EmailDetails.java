package com.erastedev.ciexplore.v1.domain.models.email;

import lombok.*;

import java.util.List;

/**
 * Model class representing the details of an email.
 * This class is used to transfer email-related data.
 *
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-24 11:58:36
 * @Description: Holds information about the recipient, subject, and body of an email.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDetails {
    /**
     * The recipient of the email.
     */
    private String recipient;

    /**
     * The subject of the email.
     */
    private String subject;

    /**
     * The body of the email.
     */
    private String body;

    /**
     * Carbon copy recipients.
     */
    private List<String> cc;

    /**
     * Blind carbon copy recipients.
     */
    private List<String> bcc;

    /**
     * Attachments associated with the email.
     */
    private List<String> attachments;
}
