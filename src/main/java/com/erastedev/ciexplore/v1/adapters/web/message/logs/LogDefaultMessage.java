/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-06 13:00:35
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:45
 * @FilePath: src/main/java/ca/deltagis/success/v1/adapters/web/message/logs/LogDefaultMessage.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package com.erastedev.ciexplore.v1.adapters.web.message.logs;

import lombok.Getter;

public enum LogDefaultMessage {
    // AUTH
    AUTH_LOGIN("Logged in successfully"),
    AUTH_LOGIN_FAILED("Login failed"),
    AUTH_LOGOUT("Logged out successfully"),
    AUTH_REGISTER("Registered successfully"),
    AUTH_RESET_PASSWORD("Password reset successfully"),
    AUTH_RESET_PASSWORD_WITH_CODE("Password reset successfully with code"),
    AUTH_FORGOT_PASSWORD("Password reset link sent successfully"),
    AUTH_FORGOT_PASSWORD_WITH_CODE("Password reset link sent successfully with code"),
    AUTH_REFRESH_TOKEN("Refreshed JWT token successfully"),
    AUTH_INVITE_USER("User invited successfully"),
    AUTH_INVITE_USER_REJECTED("User invitation has been rejected"),
    AUTH_INVITE_USER_ALREADY_INVITED("User has already been invited"),
    AUTH_INVITE_USER_EXPIRED("User invitation has expired"),
    
    // WORKSPACE
    WORKSPACE_CREATE("Workspace created successfully"),
    WORKSPACE_UPDATE("Workspace updated successfully"),
    WORKSPACE_DELETE("Workspace deleted successfully");
    
    @Getter
    private final String message;
    
    LogDefaultMessage(String message) {
        this.message = message;
    }
}
