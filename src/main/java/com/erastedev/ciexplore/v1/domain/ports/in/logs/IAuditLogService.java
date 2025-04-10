/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-14 15:07:45
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-14 15:07:45
 */
package com.erastedev.ciexplore.v1.domain.ports.in.logs;

import com.erastedev.ciexplore.v1.domain.entities.user.User;
import com.erastedev.ciexplore.v1.domain.models.AuditLogActionCode;

public interface IAuditLogService<T> {
    /**
     * @return the user who is currently logged in
     */
    User user();

    /**
     * Creates a log entry for the given user, action code, and message.
     *
     * @param actionCode the action code representing the type of action performed
     * @param message    a brief description of the action
     * @param user       the user who performed the action
     * @param t          the object that was the target of the action
     */
    void setLog(AuditLogActionCode actionCode, String message, User user, T t);

    /**
     * Creates a log entry for the given object, action code, and message when the object is created.
     * <p>
     * The action code is determined by the value of the given boolean.
     * If the boolean is true, the action code is {@link AuditLogActionCode#CREATE_FAILED},
     * otherwise the action code is {@link AuditLogActionCode#CREATE_SUCCESS}.
     * <p>
     * The message is automatically generated to be either "Created <i>objectName</i>" or
     * "Failed to create <i>objectName</i>" depending on the value of the boolean.
     *
     * @param isSuccess false if the action is a failed action, otherwise true
     */
    void logCreate(T entity, Boolean isSuccess);

    /**
     * Creates a log entry for the given object, action code, and message when the object is read.
     * <p>
     * The action code is determined by the value of the given boolean.
     * If the boolean is true, the action code is {@link AuditLogActionCode#READ_FAILED},
     * otherwise the action code is {@link AuditLogActionCode#READ_SUCCESS}.
     * <p>
     * The message is automatically generated to be either "Read <i>objectName</i>" or
     * "Failed to read <i>objectName</i>" depending on the value of the boolean.
     *
     * @param isSuccess false if the action is a failed action, otherwise true
     */
    void logRead(T entity, Boolean isSuccess);

    /**
     * Creates a log entry for the given object, action code, and message when the object is updated.
     * <p>
     * The action code is determined by the value of the given boolean.
     * If the boolean is true, the action code is {@link AuditLogActionCode#UPDATE_FAILED},
     * otherwise the action code is {@link AuditLogActionCode#UPDATE_SUCCESS}.
     * <p>
     * The message is automatically generated to be either "Updated <i>objectName</i>" or
     * "Failed to update <i>objectName</i>" depending on the value of the boolean.
     *
     * @param isSuccess false if the action is a failed action, otherwise true
     */
    void logUpdate(T entity, Boolean isSuccess);

    /**
     * Creates a log entry for the given object, action code, and message when the object is deleted.
     * <p>
     * The action code is determined by the value of the given boolean.
     * If the boolean is true, the action code is {@link AuditLogActionCode#DELETE_FAILED},
     * otherwise the action code is {@link AuditLogActionCode#DELETE_SUCCESS}.
     * <p>
     * The message is automatically generated to be either "Deleted <i>objectName</i>" or
     * "Failed to delete <i>objectName</i>" depending on the value of the boolean.
     *
     * @param isSuccess false if the action is a failed action, otherwise true
     *
     */
    void logDelete(T entity, Boolean isSuccess, String Message);
}
