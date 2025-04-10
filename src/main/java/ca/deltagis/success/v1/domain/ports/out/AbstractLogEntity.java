/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-06 20:38:34
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-06 20:38:58
 */
package ca.deltagis.success.v1.domain.ports.out;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class AbstractLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actionCode;
    private String message;
    private String userEmail;
    private LocalDateTime timestamp = LocalDateTime.now();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the log entry.
     *
     * @param id the ID of the log entry to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves the action code associated with the log entry.
     *
     * @return the action code as a String
     */
    public String getActionCode() {
        return actionCode;
    }

    /**
     * Sets the action code associated with the log entry.
     *
     * @param actionCode the action code to be associated with the log entry
     */
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    /**
     * Retrieves the message associated with the log entry.
     *
     * @return the message associated with the log entry.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message associated with the log entry.
     *
     * @param message the message to associate with the log entry
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Retrieves the email address of the user associated with the log entry.
     *
     * @return the email address of the user associated with the log entry
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Sets the email address of the user associated with the log entry.
     *
     * @param userEmail the email address of the user associated with the log entry
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Retrieves the timestamp of the log entity.
     *
     * @return the date and time when the log was created or last modified.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the log entity.
     *
     * @param timestamp the date and time to set as the log's timestamp.
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
