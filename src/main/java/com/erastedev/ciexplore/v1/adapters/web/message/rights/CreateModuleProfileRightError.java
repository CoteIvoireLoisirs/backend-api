/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-25 08:58:58
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-25 08:58:58
 */
package com.erastedev.ciexplore.v1.adapters.web.message.rights;

public enum CreateModuleProfileRightError {
    NONE("none"),
    INVALID_RIGHT_STRING("INVALID_RIGHT_STRING"),
    INVALID_INPUT("INVALID_INPUT"),
    MODULE_NOT_FOUND("MODULE_NOT_FOUND"),
    INVALID_PROFILE("INVALID_PROFILE"),
    PROFILE_ALREADY_EXISTS("PROFILE_ALREADY_EXISTS"),
    NOT_FOUND("NOT_FOUND"),
    ID_REQUIRED("ID_REQUIRED"),
    UNKNOWN_ERROR("unknown_error");

    private String error;

    CreateModuleProfileRightError(String error) {
        this.error = error;
    }

    /**
     * Retrieves the error message associated with this enum constant.
     *
     * @return the error message as a String.
     */
    public String getError() {
        return error;
    }
}
