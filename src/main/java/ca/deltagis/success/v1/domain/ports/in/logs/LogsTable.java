package ca.deltagis.success.v1.domain.ports.in.logs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-25 13:26:23
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-25 13:27:46
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface LogsTable {
    /**
     * NOT IMPLEMENTED
     */
    String[] eventType() default "all";

    /**
     * The scope of the annotated class. The scope is used to group logically
     * adjacent objects (e.g. METADATA)
     *
     * @return an {@see AuditScope}
     */
    LogScope scope();
}
