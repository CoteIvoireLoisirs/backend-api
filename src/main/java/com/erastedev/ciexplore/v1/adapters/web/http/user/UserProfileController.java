/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-28 09:46:58
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-28 09:46:59
 */
package com.erastedev.ciexplore.v1.adapters.web.http.user;

import com.erastedev.ciexplore.v1.adapters.web.api.endpoints.ApiEndpoints;
import com.erastedev.ciexplore.v1.adapters.web.api.builder.ApiBuilder;
import com.erastedev.ciexplore.v1.adapters.web.api.service.ApiResponseService;
import com.erastedev.ciexplore.v1.application.request.user.AssociateUserWorkspaceRequest;
import com.erastedev.ciexplore.v1.application.request.user.SaveUserProfileResponse;
import com.erastedev.ciexplore.v1.application.services.logs.LogServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.UserAuthServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.UserProfileServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.UserServiceImpl;
import com.erastedev.ciexplore.v1.domain.entities.user.UserProfile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.USER_PROFILE)
@Tag(name = "User Profile API", description = "Operations related to user profile")
public class UserProfileController {
    //  extends AbstractCommonController<UserProfile>

    @Autowired
    private UserProfileServiceImpl service;

    @Autowired
    private UserAuthServiceImpl userAuthService;

    @Getter
    private Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private LogServiceImpl<UserProfile> logService;

    @Autowired
    private ApiResponseService response;

    public Class<?> className() {
        return this.getClass();
    }

    public UserProfileController(ApiResponseService apiResponseService, UserServiceImpl userServiceImpl, LogServiceImpl logService) {
    }

    /**
     * Creates a new entity.
     *
     * @param userId The ID of the user.
     * @return ResponseEntity containing the created entity or an error message.
     */
    @Operation(summary = "Get all user profiles for one user", description = "Retrieves a list of all user profiles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User workspace profile"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse<List<UserProfile>>> getUserProfile(
            @RequestParam int userId
    ) {
        try {
            List<UserProfile> result = service.getUserProfiles(userId);
            return new ApiBuilder<List<UserProfile>>().success("Successfully retrieved user profiles", result);
        } catch (Exception e) {
            e.printStackTrace();
            return response.internalError("Failed to delete user", e);
        }
    }

    /**
     * Associates a user to a workspace.
     *
     * <p>
     * This method will associate a user to a workspace, given a list of
     * {@link AssociateUserWorkspaceRequest} objects. If the operation is
     * successful, a response containing the associated user profiles will be
     * returned. In case of errors, a response with a bad request status code
     * will be returned.
     * </p>
     *
     * @param param the list of {@link AssociateUserWorkspaceRequest} objects to
     *              associate
     * @return a response containing the associated user profiles or an error
     * message
     */
    @Operation(summary = "Associate user to workspace", description = "Associates a user to a workspace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully associated user to workspace"),
            @ApiResponse(responseCode = "400", description = "Failed to associate user"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse<List<UserProfile>>> associateUserToWorkspace(
            @RequestBody AssociateUserWorkspaceRequest param
    ) {
        try {
            SaveUserProfileResponse result = service.associateUserToWorkspace(param);
            if (result.getError() != null) {
                String error = result.getErrors().toString();
                return new ApiBuilder<List<UserProfile>>().badResponse(error, "Failed to associate user");
            }

            return new ApiBuilder<List<UserProfile>>().success("Successfully associated user to workspace", result.getUserProfiles());
        } catch (Exception e) {
            e.printStackTrace();
            return response.internalError("Failed to associate user", e);
        }
    }
}
