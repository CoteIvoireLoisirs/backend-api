package com.erastedev.ciexplore.v1.application.validator.in;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommonValidation<T, Error> {
    private final T data;
    private Error error;
    private final Boolean success;

    public CommonValidation(T data, Error error) {
        this.data = data;
        this.error = error;
        this.success = false;
    }

    public CommonValidation(T data, Error error, Boolean success) {
        this.data = data;
        this.error = error;
        this.success = success;
    }
}
