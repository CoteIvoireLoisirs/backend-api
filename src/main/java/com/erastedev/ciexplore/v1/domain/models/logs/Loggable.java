/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-06 20:42:59
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-06 20:42:59
 */
package com.erastedev.ciexplore.v1.domain.models.logs;

import com.erastedev.ciexplore.v1.domain.models.AuditLogActionCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {

    /**
     * The action code that describes the action performed when the annotated
     * method is successful.
     *
     * @return the action code
     */
    AuditLogActionCode action();

    /**
     * The action code that describes the action performed when the annotated
     * method fails.
     *
     * @return the action code
     */
    AuditLogActionCode actionFailed();

    /**
     * The message associated with the log entry.
     * This message provides additional context about the action
     * performed by the annotated method.
     *
     * @return the message as a String
     */
    String message() default "";
}
