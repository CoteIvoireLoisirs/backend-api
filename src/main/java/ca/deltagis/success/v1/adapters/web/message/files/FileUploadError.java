/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-04 06:41:54
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:46
 * @FilePath: src/main/java/ca/deltagis/success/v1/adapters/web/message/files/FileUploadError.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package ca.deltagis.success.v1.adapters.web.message.files;

import ca.deltagis.success.v1.adapters.web.message.WorkspaceMessage;
import ca.deltagis.success.v1.adapters.web.message.CompanyMessage;
import ca.deltagis.success.v1.adapters.web.message.ProjectMessage;

public enum FileUploadError {
    EMPTY_FILE("EMPTY_FILE"),
    INVALID_FILE("INVALID_FILE"),
    INVALID_FILE_NAME("INVALID_FILE_NAME"),
    INVALID_FILE_TYPE("INVALID_FILE_TYPE : %s allowed"),
    FILE_NOT_FOUND("FILE_NOT_FOUND"),
    FILE_SIZE_EXCEEDED("FILE_SIZE_EXCEEDED"),
    FILE_TYPE_NOT_SUPPORTED("FILE_TYPE_NOT_SUPPORTED"),
    INVALID_DESTINATION_PATH("INVALID_DESTINATION_PATH"),
    FAILED_TO_SAVE_FILE("FAILED_TO_SAVE_FILE"),
    SOMETHING_WENT_WRONG("SOMETHING_WENT_WRONG");

    private final String error;

    FileUploadError(String value) {
        this.error = value;
    }

    /**
     * Converts a given {@link WorkspaceMessage} to its corresponding {@link FileUploadError}.
     *
     * @param error the {@link WorkspaceMessage} to convert
     * @return the corresponding {@link FileUploadError}, or null if no match is found
     */
    public static FileUploadError fromFileUploadError(WorkspaceMessage error) {
        return switch (error) {
            case CANT_UPLOAD_EMPTY_FILE -> EMPTY_FILE;
            case UPLOAD_INVALID_DESTINATION -> INVALID_DESTINATION_PATH;
            case UPLOAD_FAILED -> FAILED_TO_SAVE_FILE;
            default -> null;
        };
    }

    /**
     * Converts a given {@link ProjectMessage} to its corresponding {@link FileUploadError}.
     *
     * @param error the {@link ProjectMessage} to convert
     * @return the corresponding {@link FileUploadError}, or null if no match is found
     */
    public static FileUploadError fromFileUploadError(ProjectMessage error) {
        return switch (error) {
            case CANT_UPLOAD_EMPTY_FILE -> EMPTY_FILE;
            case UPLOAD_INVALID_DESTINATION -> INVALID_DESTINATION_PATH;
            case UPLOAD_FAILED -> FAILED_TO_SAVE_FILE;
            default -> null;
        };
    }

    /**
     * Converts a given {@link CompanyMessage} to its corresponding {@link FileUploadError}.
     *
     * @param error the {@link CompanyMessage} to convert
     * @return the corresponding {@link FileUploadError}, or null if no match is found
     */
    public static FileUploadError fromFileUploadError(CompanyMessage error) {
        return switch (error) {
            case CANT_UPLOAD_EMPTY_FILE -> EMPTY_FILE;
            case UPLOAD_INVALID_DESTINATION -> INVALID_DESTINATION_PATH;
            case UPLOAD_FAILED -> FAILED_TO_SAVE_FILE;
            default -> null;
        };
    }
}
