package com.erastedev.ciexplore.v1.domain.entities.user.exception;

public class InvalidEmailException extends Exception {
    public InvalidEmailException(String message) {
        super(message);
    }
}