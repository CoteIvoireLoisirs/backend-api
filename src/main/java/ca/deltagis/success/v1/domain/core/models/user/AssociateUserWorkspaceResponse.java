package ca.deltagis.success.v1.domain.core.models.user;

import ca.deltagis.success.v1.domain.core.entities.user.User;
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
