package ca.deltagis.success.v1.adapters.web.message;

import ca.deltagis.success.v1.adapters.web.message.files.FileUploadError;

public enum CompanyMessage {

    ALREADY_EXISTS("YOUR COMPANY IS ALREADY EXISTE"),
    OK("OK"),
    CREATED("YOUR COMPANY HAS BEEN CREATED"),
    NOT_FOUND("NOT_FOUND"),
    SOMETHING_WENT_WRONG("SOMETHING WENT WRONG"),
    WORKSPACE_NOT_FOUND("WORKSPACE NOT FOUND"),
    UPDATED("EXERCICE UPDATED"),
    UPLOAD_INVALID_DESTINATION("INVALID DESTINATION"),
    UPLOAD_FAILED("FAILED TO UPLOAD FILE"),
    INVALID_OPERATION("operation impossible"),

    CANT_UPLOAD_EMPTY_FILE("YOU CAN NOT UPLOAD EMPTY FILE"),
    NOT_UPDATED("MODIFICATION IMPOSSIBLE PLEASE CHECK THE STATUS OF THE EXERCICE PLEASE"),
    NONE("NONE");

    private final String value;

    CompanyMessage(String value) {
        this.value = value;
    }

    public static CompanyMessage fromFileUploadError(FileUploadError error) {
        return switch (error) {
            case EMPTY_FILE -> CANT_UPLOAD_EMPTY_FILE;
            case INVALID_DESTINATION_PATH -> UPLOAD_INVALID_DESTINATION;
            case FAILED_TO_SAVE_FILE -> UPLOAD_FAILED;
            default -> SOMETHING_WENT_WRONG;
        };
    }

}
