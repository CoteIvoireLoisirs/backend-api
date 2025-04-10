/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-07 15:17:11
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-07 15:17:11
 */
package com.erastedev.ciexplore.v1.adapters.web.message.user;

public enum AuthLoginError {
    NONE("NONE"),
    USER_NOT_FOUND("USER_NOT_FOUND"),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS"),
    CANT_ACCESS_WORKSPACE("CANT_ACCESS_WORKSPACE"),
    ALREADY_LOGGED_IN("ALREADY_LOGGED_IN");

    private final String error;

    AuthLoginError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return error;
    }
}
