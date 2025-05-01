package com.erastedev.ciexplore.v1.application.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyEmailRequest {
    @NotBlank
    @NotNull
    @NotEmpty
    public String email;

    @NotBlank
    @NotNull
    @NotEmpty
    public String code;
}
