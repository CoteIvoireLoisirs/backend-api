/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-29 19:33:08
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-29 19:33:08
 */
package com.erastedev.ciexplore.v1.domain.models.user;

import com.erastedev.ciexplore.v1.adapters.web.message.files.FileUploadError;

public enum UserRegisterState {
    SUCCESS,
    ALREADY_REGISTERED,
    INVALID_EMAIL,
    EMAIL_ALREADY_USED,
    USERNAME_ALREADY_USED,
    INVALID_USERNAME,
    USER_NOT_FOUND,
    USER_DOES_NOT_HAVE_ACCESS_TO_WORKSPACE,
    PASSWORD_NOT_FOUND,
    WORKSPACE_NOT_FOUND,
    WORKSPACE_IS_NOT_ACTIVE,
    USER_IS_IN_ANOTHER_WORKSPACE,
    INVALID_TOKEN,
    INVALID_PASSWORD,
    INVALID_INVITATION_CODE,
    SOMETHING_WENT_WRONG,
    EXPIRED_INVITATION_CODE,
    UPDATE_USERNAME_DENIED,
    UPDATE_EMAIL_DENIED,
    CANT_UPLOAD_EMPTY_FILE,
    UPLOAD_INVALID_DESTINATION,
    UPLOAD_FAILED;

    private final String name;

    UserRegisterState() {
        this.name = name();
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns a {@link UserRegisterState} corresponding to a given {@link FileUploadError}.
     *
     * @param error the {@link FileUploadError} to convert
     * @return the corresponding {@link UserRegisterState}
     */
    public static UserRegisterState fromFileUploadError(FileUploadError error) {
        if (error == null) {
            return null;
        }
        return switch (error) {
            case EMPTY_FILE -> CANT_UPLOAD_EMPTY_FILE;
            case INVALID_DESTINATION_PATH -> UPLOAD_INVALID_DESTINATION;
            case FAILED_TO_SAVE_FILE -> UPLOAD_FAILED;
            default -> SOMETHING_WENT_WRONG;
        };
    }

    /**
     * Converts a given {@link UserRegisterState} to its corresponding {@link FileUploadError}.
     *
     * @param error the {@link UserRegisterState} to convert
     * @return the corresponding {@link FileUploadError}, or null if no match is found
     */
    public static FileUploadError fromFileUploadError(UserRegisterState error) {
        if (error == null) {
            return null;
        }
        return switch (error) {
            case CANT_UPLOAD_EMPTY_FILE -> FileUploadError.EMPTY_FILE;
            case UPLOAD_INVALID_DESTINATION -> FileUploadError.INVALID_DESTINATION_PATH;
            case UPLOAD_FAILED -> FileUploadError.FAILED_TO_SAVE_FILE;
            default -> null;
        };
    }
}
