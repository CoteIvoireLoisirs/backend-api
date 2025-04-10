package com.erastedev.ciexplore.v1.domain.ports.in.logs;

import com.erastedev.ciexplore.v1.domain.ports.in.ICommonEntity;
import org.apache.commons.logging.Log;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-25 13:30:09
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-25 13:30:12
 */
public class Logs {
    public static final String ACTION_CREATE = "create";
    public static final String ACTION_READ = "read";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_DELETE = "delete";

    public static final String ACTION_CREATE_DENIED = "create denied";
    public static final String ACTION_READ_DENIED = "read denied";
    public static final String ACTION_UPDATE_DENIED = "update denied";
    public static final String ACTION_DELETE_DENIED = "delete denied";

    public static void infoWrapper(Log log, String username, Object object, String action) {
        if (log.isInfoEnabled()) {
            if (username != null && object != null && ICommonEntity.class.isInstance(object)) {
                ICommonEntity idObject = (ICommonEntity) object;
                StringBuilder builder = new StringBuilder();

                builder.append("'").append(username).append("' ").append(action);

                builder.append(" ").append(object.getClass().getSuperclass().getName());

                if (idObject.getDisplayName() != null && !idObject.getDisplayName().isEmpty()) {
                    builder.append(", name: ").append(idObject.getDisplayName());
                }

                if (idObject.getId() != null) {
                    builder.append(", id: ").append(idObject.getId());
                }

                log.info(builder.toString());
            }
        }
    }
}
