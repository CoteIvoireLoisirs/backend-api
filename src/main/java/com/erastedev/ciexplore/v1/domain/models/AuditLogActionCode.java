/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-06 15:28:07
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-06 15:31:41
 */
package com.erastedev.ciexplore.v1.domain.models;

public enum AuditLogActionCode {
    // AUTH ACTION
    AUTH_LOGIN_SUCCESS,
    AUTH_LOGIN_FAILED,
    AUTH_LOGOUT,
    AUTH_REFRESH,
    AUTH_RESET_PASSWORD,
    AUTH_RECOVERY_CODE,
    AUTH_FORGOT_PASSWORD,
    AUTH_FORGOT_PASSWORD_WITH_CODE,
    AUTH_REGISTER,
    AUTH_INVITE_USER,
    AUTH_INVITE_USER_REJECTED,
    AUTH_INVITE_USER_ALREADY_INVITED,

    // WORKSPACE ACTION
    WORKSPACE_CREATE,
    WORKSPACE_UPDATE,
    WORKSPACE_DELETE,

    // PROJECT ACTION
    PROJECT_CREATE,
    PROJECT_UPDATE,
    PROJECT_DELETE,

    // DEFAULT CRUD ACTION
    CREATE_SUCCESS,
    READ_SUCCESS,
    UPDATE_SUCCESS,
    DELETE_SUCCESS,
    CREATE_FAILED,
    READ_FAILED,
    UPDATE_FAILED,
    DELETE_FAILED;

    private AuditLogActionCode() {
    }

    public String getCode() {
        return this.name().toLowerCase();
    }
}
