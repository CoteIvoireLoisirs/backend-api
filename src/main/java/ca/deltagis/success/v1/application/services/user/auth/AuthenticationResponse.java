package ca.deltagis.success.v1.application.services.user.auth;

import ca.deltagis.success.v1.adapters.web.message.user.AuthLoginError;
import ca.deltagis.success.v1.domain.core.entities.user.User;
import ca.deltagis.success.v1.domain.core.entities.user.UserProfile;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

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
