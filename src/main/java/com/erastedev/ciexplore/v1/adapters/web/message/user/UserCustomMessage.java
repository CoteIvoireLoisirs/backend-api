package com.erastedev.ciexplore.v1.adapters.web.message.user;

public enum UserCustomMessage {
    USER_NOT_FOUND("User not found"),
    USER_ALREADY_EXISTS("User already exists"),
    INVALID_CREDENTIALS("Invalid credentials"),
    USER_DISABLED("User disabled"),
    INTERNAL_ERROR("Internal error"),
    ALREADY_LOGGED("User already logged");

    private final String message;

    UserCustomMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the message of this custom message.
     *
     * @return The message of this custom message.
     */
    public String getMessage() {
        return message;
    }
}
