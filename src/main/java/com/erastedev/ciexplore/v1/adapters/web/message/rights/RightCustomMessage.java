/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-10 10:02:32
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:46
 * @FilePath: src/main/java/ca/deltagis/success/v1/adapters/web/message/rights/RightCustomMessage.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package com.erastedev.ciexplore.v1.adapters.web.message.rights;

public enum RightCustomMessage {

    CANNOT_UPDATE_SYSTEM_RIGHT("CANNOT_UPDATE_SYSTEM_RIGHT");

    private final String message;

    RightCustomMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the message of this custom message.
     *
     * @return The message of this custom message.
     */
    public String getMessage() {
        return message;
    }
}
