package ca.deltagis.success.v1.application.validator.out;

import ca.deltagis.success.v1.adapters.web.message.user.UserProfileError;
import ca.deltagis.success.v1.application.request.user.AssociateUserWorkspaceRequest;
import ca.deltagis.success.v1.application.request.user.SaveUserProfileResponse;
import ca.deltagis.success.v1.application.services.rights.ModuleProfileRightServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserServiceImpl;
import ca.deltagis.success.v1.application.services.workspace.WorkspaceServiceImpl;
import ca.deltagis.success.v1.application.validator.in.CommonValidation;
import ca.deltagis.success.v1.domain.core.entities.rights.ModuleProfileRight;
import ca.deltagis.success.v1.domain.core.entities.user.User;
import ca.deltagis.success.v1.domain.core.entities.workspace.Workspace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AssociateUserWorkspaceValidation {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    WorkspaceServiceImpl workspaceService;

    @Autowired
    ModuleProfileRightServiceImpl moduleProfileRightService;

    Logger logger = LoggerFactory.getLogger(AssociateUserWorkspaceValidation.class);

    /**
     * Validate the given {@link AssociateUserWorkspaceRequest} and return a
     * {@link SaveUserProfileResponse} containing errors if any validation fails.
     *
     * @param request the request to validate
     * @return a {@link SaveUserProfileResponse} or null if validation succeeds
     */
    private SaveUserProfileResponse validate(AssociateUserWorkspaceRequest request) {
        HashMap<String, String> map = new HashMap<>();

        // check Workspace
        Workspace checkWorkspace = workspaceService.getByCode(request.getWorkspaceCode()).orElse(null);
        if (checkWorkspace == null || checkWorkspace.getDeleted() != null) {
            map.put("workspaceCode", "Workspace not found");
            return new SaveUserProfileResponse(null, UserProfileError.WORKSPACE_NOT_FOUND, map);
        }

        // check user
        User checkUser = userService.getByIdOptional(request.getUserId()).orElse(null);
        if (checkUser == null || checkUser.getDeleted() != null) {
            map.put("userId", "User not found");
            return new SaveUserProfileResponse(null, UserProfileError.USER_NOT_FOUND, map);
        }

        // Check Module Profile Right
        ModuleProfileRight profileRight = moduleProfileRightService.getModuleProfileRight(request.getModuleProfileRightId());
        if (profileRight == null || profileRight.getDeleted() != null) {
            map.put("moduleProfileRightId", "Module Profile Right not found");
            return new SaveUserProfileResponse(null, UserProfileError.MODULE_PROFILE_RIGHT_NOT_FOUND, map);
        }

        return null;
    }

    public SaveUserProfileResponse associateUserToWorkspaceValidation(AssociateUserWorkspaceRequest request) {
        HashMap<String, SaveUserProfileResponse> map = new HashMap<>();


        SaveUserProfileResponse response = validate(request);
        logger.info("Validation response: {}, for item: {}", response, request);
        if (response != null) {
            map.put(request.getWorkspaceCode() + "_" + request.getUserId(), response);
        }

        if (!map.isEmpty()) {
            HashMap<String, String> mapErrors = new HashMap<>();
            map.forEach((key, value) -> mapErrors.put(key, value.getError().getError()));
            return new SaveUserProfileResponse(null, UserProfileError.SOMETHING_WENT_WRONG, mapErrors);
        }

        return null;
    }
}
