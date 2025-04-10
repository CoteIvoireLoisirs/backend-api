package com.erastedev.ciexplore.v1.application.request.user;

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
