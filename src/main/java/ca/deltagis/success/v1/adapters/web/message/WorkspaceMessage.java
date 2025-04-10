/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-12 12:18:42
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-12 12:19:09
 */
package ca.deltagis.success.v1.adapters.web.message;

import ca.deltagis.success.v1.adapters.web.message.files.FileUploadError;

public enum WorkspaceMessage {
    NONE("NONE"),
    NOT_FOUND("NOT_FOUND"),
    CANT_UPLOAD_EMPTY_FILE("CANT_UPLOAD_EMPTY_FILE"),
    UPLOAD_INVALID_DESTINATION("UPLOAD_INVALID_DESTINATION"),
    UPLOAD_FAILED("UPLOAD_FAILED"),
    ALREADY_EXISTS("ALREADY_EXISTS"),
    ALREADY_PENDING("ALREADY_PENDING"),
    ACTIVATED_SUCCESSFULLY("ACTIVATED_SUCCESSFULLY"),
    DISABLE("YOUR WOKSPACE HAS BEEN SUCCESSFULLY DEACTIVATED"),
    ALREADY_DISABLE("YOUR WAKSPACE IS ALREADY DESABLE"),
    ERROR_STATUS("IMPOSSIBLE DEACTIVATION,CHECK THE SATUTS OF THE WORKSPACE"),
    SOMETHING_WENT_WRONG("SOMETHING WENT WRONG"),
    UPDATED("Workspace updated successfully");

    private final String value;

    WorkspaceMessage(String value) {
        this.value = value;
    }

     public static WorkspaceMessage fromFileUploadError(FileUploadError error) {
        return switch (error) {
            case EMPTY_FILE -> CANT_UPLOAD_EMPTY_FILE;
            case INVALID_DESTINATION_PATH -> UPLOAD_INVALID_DESTINATION;
            case FAILED_TO_SAVE_FILE -> UPLOAD_FAILED;
            default -> SOMETHING_WENT_WRONG;
        };
    }
}
