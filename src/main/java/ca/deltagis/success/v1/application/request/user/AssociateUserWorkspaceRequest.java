package ca.deltagis.success.v1.application.request.user;

import ca.deltagis.success.v1.domain.core.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssociateUserWorkspaceRequest {
    Long userId;
    String workspaceCode;
    Long moduleProfileRightId;
}
