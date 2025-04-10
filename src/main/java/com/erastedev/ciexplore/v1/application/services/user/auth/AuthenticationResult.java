/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-07 15:59:16
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-07 15:59:16
 */
package com.erastedev.ciexplore.v1.application.services.user.auth;

import com.erastedev.ciexplore.v1.adapters.web.message.user.AuthLoginError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResult {
    private String token;
    private AuthLoginError error;

    /**
     * Returns an AuthenticationResult instance with a valid token and no error.
     *
     * @param token the valid authentication token
     * @return an AuthenticationResult instance with a valid token and no error
     */
    public static AuthenticationResult success(String token) {
        return new AuthenticationResult(token, null);
    }

    /**
     * Returns an AuthenticationResult instance with a null token and the given error.
     *
     * @param error the AuthLoginError that caused the failure
     * @return an AuthenticationResult instance with a null token and the given error
     */
    public static AuthenticationResult failure(AuthLoginError error) {
        return new AuthenticationResult(null, error);
    }

    /**
     * Returns the authentication token, or null if the authentication failed.
     *
     * @return the authentication token, or null if the authentication failed
     */
    public String getToken() {
        return token;
    }

    /**
     * Returns the error that caused the authentication to fail, or null if the authentication succeeded.
     *
     * @return the error that caused the authentication to fail, or null if the authentication succeeded
     */
    public AuthLoginError getError() {
        return error;
    }

    /**
     * Indicates whether the authentication succeeded or not.
     *
     * @return true if the authentication succeeded, false if it failed
     */
    public boolean isSuccess() {
        return token != null;
    }
}
