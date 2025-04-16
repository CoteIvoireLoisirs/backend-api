package com.erastedev.ciexplore.v1.domain.entities.user.exception;

public class InvalidPasswordException extends Exception {
    public InvalidPasswordException(String message) {
        super(message);
    }
}