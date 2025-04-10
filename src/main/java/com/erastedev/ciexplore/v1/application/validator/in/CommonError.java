package com.erastedev.ciexplore.v1.application.validator.in;

import com.erastedev.ciexplore.v1.adapters.web.message.files.FileUploadError;

public enum CommonError {
    // USER
    USER_NOT_ADMIN,

    SOMETHING_WENT_WRONG,
    CANT_UPLOAD_EMPTY_FILE,
    UPLOAD_INVALID_DESTINATION,
    UPLOAD_FAILED;

    private final String name;

    CommonError() {
        this.name = name();
    }

    @Override
    public String toString() {
        return name;
    }

    public static CommonError fromFileUploadError(FileUploadError error) {
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

    public static FileUploadError fromFileUploadError(CommonError error) {
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
