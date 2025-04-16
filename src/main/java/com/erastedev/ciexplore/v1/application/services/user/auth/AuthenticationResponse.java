package com.erastedev.ciexplore.v1.application.services.user.auth;

import com.erastedev.ciexplore.v1.adapters.web.message.user.AuthLoginError;
import com.erastedev.ciexplore.v1.domain.entities.user.model.User;
import lombok.Data;

import java.util.HashMap;

@Data
public class AuthenticationResponse {
    AuthenticationRecord auth;
    User user;
    HashMap<String, HashMap<String, Boolean>> rights = null;
    // List<UserProfile> rights = null;
    AuthLoginError error;

    public AuthenticationResponse(AuthenticationRecord auth, User user, HashMap<String, HashMap<String, Boolean>> rights) {
        this.auth = auth;
        this.user = user;
        this.error = AuthLoginError.NONE;
        this.rights = rights;
    }

    public AuthenticationResponse(AuthenticationRecord auth, User user) {
        this.auth = auth;
        this.user = user;
        this.error = AuthLoginError.NONE;
    }
}
