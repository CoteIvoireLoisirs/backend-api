package com.erastedev.ciexplore.v1.domain.models.user;

import com.erastedev.ciexplore.v1.domain.entities.user.User;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
public class AssociateUserWorkspaceResponse {
    private final User user;
    private UserRegisterState state;
    private List<HashMap<String, Boolean>> workspaceValidation;
    private final Boolean success;
}
