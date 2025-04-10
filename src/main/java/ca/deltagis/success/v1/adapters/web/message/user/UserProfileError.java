/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-28 10:15:48
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-28 10:15:48
 */
package ca.deltagis.success.v1.adapters.web.message.user;

import lombok.Getter;

@Getter
public enum UserProfileError {
    NONE("NONE"),
    MODULE_PROFILE_RIGHT_NOT_FOUND("MODULE_PROFILE_RIGHT_NOT_FOUND"),
    USER_NOT_FOUND("USER_NOT_FOUND"),
    WORKSPACE_NOT_FOUND("WORKSPACE_NOT_FOUND"),
    WORKSPACE_CODE_REQUIRED("WORKSPACE_CODE_REQUIRED"),
    USER_ID_NOT_MATCH("USER_ID_NOT_MATCH"),
    PROFILE_ID_REQUIRED("PROFILE_ID_REQUIRED"),
    PROFILE_NOT_FOUND("PROFILE_NOT_FOUND"),
    USER_ID_REQUIRED("USER_ID_REQUIRED"),
    ALREADY_EXISTS("ALREADY_EXISTS"),
    SAVE_FAILED("SAVE_FAILED"),
    SOMETHING_WENT_WRONG("SOMETHING_WENT_WRONG"),
    UNKNOWN_ERROR("UNKNOWN_ERROR");

    private final String error;

    UserProfileError(String error) {
        this.error = error;
    }

}
