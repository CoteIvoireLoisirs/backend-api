package com.erastedev.ciexplore.v1.adapters.web.http.user;

import com.erastedev.ciexplore.v1.application.validator.in.CommonValidation;
import com.erastedev.ciexplore.v1.application.validator.in.CommonError;
import com.erastedev.ciexplore.v1.adapters.web.api.endpoints.ApiEndpoints;
import com.erastedev.ciexplore.v1.adapters.web.api.ApiResponse;
import com.erastedev.ciexplore.v1.adapters.web.api.builder.ApiBuilder;
import com.erastedev.ciexplore.v1.adapters.web.api.service.ApiResponseService;
import com.erastedev.ciexplore.v1.adapters.web.message.files.FileUploadError;
import com.erastedev.ciexplore.v1.application.services.user.UserServiceImpl;
import com.erastedev.ciexplore.v1.domain.entities.user.User;
import com.erastedev.ciexplore.v1.domain.models.FileUploadResponse;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserDeleteResponse;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserRegisterAttempt;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserRegisterState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.util.List;

@RestController
@RequestMapping(ApiEndpoints.USERS)
@Tag(name = "User API", description = "Operations related to User management")
//  extends AbstractCommonController<User>
public class UserController {

    @Autowired
    private UserServiceImpl service;

    @Getter
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ApiResponseService response;

    /**
     * Retrieves all users.
     * <p>
     * This method fetches a list of all users from the repository. If successful, a response
     * with a 200 status code is returned. In case of an internal server error, a 500 status
     * code is returned.
     * </p>
     *
     * @return a ResponseEntity containing the list of users or an error message
     */
    @Operation(summary = "Get all users in the system (Only for admin)", description = "Gets all users in the system (Only for admin)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers(@RequestParam @Nullable String workspaceCode) {
        try {
            CommonValidation<List<User>, CommonError> users = service.getAllUsers(workspaceCode);
            if (users.getError() == null) {
                return new ApiBuilder<List<User>>().success("User retrieved successfully", users.getData());
            }

            return new ApiBuilder<List<User>>().badResponse(users.getError().toString(), "Failed to retrieve user");
        } catch (Exception e) {
            return response.internalError("Failed to retrieve user", e);
        }
    }

    /**
     * Creates a new user without association.
     * <p>
     * This operation will create a new user without associating it to any workspace.
     * </p>
     *
     * @param param the user to create
     * @return a response containing the created user
     */
    @Operation(summary = "Create a new user without association", description = "Creates a new user without association")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to create user"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUserWithoutAssociation(@RequestBody User param) {
        try {
            UserRegisterAttempt attempt = service.createUserWithoutAssociation(param);
            if (!attempt.getSuccess() && attempt.getState() != null) {
                return new ApiBuilder<User>().badResponse(attempt.getState().name(), "Failed to create user");
            }

            return new ApiBuilder<User>().success("User created successfully", param);
        } catch (Exception e) {
            e.printStackTrace();
            return response.internalError("Failed to create user", e);
        }
    }

    /**
     * Creates a new user from a workspace.
     * <p>
     * Creates a new user from a workspace.
     *
     * @param workspaceCode the workspace code
     * @param param         the user to create
     * @return a response containing the created user
     */
    @Operation(summary = "Create a new user and associate it to a workspace", description = "Creates a new user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to create user"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("workspace/{workspaceCode}")
    public ResponseEntity<ApiResponse<User>> createUserAndAssociateToWorkspace(@PathVariable String workspaceCode, @RequestBody User param) {
        try {
            logger.info("Creating user {} from workspace {}", param, workspaceCode);
            UserRegisterAttempt userRegisterAttempt = service.createUserFromWorkspace(param, workspaceCode);
            if (!userRegisterAttempt.getSuccess() && userRegisterAttempt.getState() != null) {
                return new ApiBuilder<User>().badResponse(userRegisterAttempt.getState().name(), "Failed to create user");
            }

            return new ApiBuilder<User>().success("User created successfully", param);
        } catch (Exception e) {
            e.printStackTrace();
            return response.internalError("Failed to create user", e);
        }
    }

    /**
     * Deletes a user.
     *
     * <p>
     * This operation attempts to delete a user identified by the given <code>userId</code>. If the
     * user is found and deleted successfully, a response with a 200 status code is returned. If the
     * user is not found, a 404 status code is returned. In case of an internal server error, a 500
     * status code is returned.
     *
     * @param userId The ID of the user to delete.
     * @return A ResponseEntity containing the result of the delete operation.
     */
    @Operation(summary = "Delete a user", description = "Deletes a user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to delete user"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("{userId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteUser(@PathVariable Long userId) {
        try {
            UserDeleteResponse attempt = service.deleteUser(userId);
            if (!attempt.getSuccess() && attempt.getState() != null) {
                return new ApiBuilder<Boolean>().badResponse(attempt.getState().name(), "Failed to delete user");
            }

            return new ApiBuilder<Boolean>().success("Delete user successfully", true);
        } catch (Exception e) {
            e.printStackTrace();
            return response.internalError("Failed to delete user", e);
        }
    }

    /**
     * Retrieves a single user identified by the given userId.
     * <p>
     * If the user is found, a response with a 200 status code is returned. If the user is not
     * found, a 404 status code is returned. In case of an internal server error, a 500 status
     * code is returned.
     * <p>
     * The response contains a json object with the following fields:
     * <ul>
     * <li>user - the retrieved user</li>
     * <li>errorType - the type of error if the user was not found</li>
     * <li>message - a message describing the result of the operation</li>
     * </ul>
     *
     * @param userId the ID of the user to retrieve
     * @return a ResponseEntity containing the result of the operation
     */
    @Operation(summary = "Get a single user", description = "Gets a single user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getSingleUser(@Parameter @PathVariable Long userId) {
        try {
            User user = service.getUser(userId);
            if (user == null) {
                return new ApiBuilder<User>().notFound("User not found", "User with ID " + userId + " not found");
            }

            return new ApiBuilder<User>().success("User retrieved successfully", user);
        } catch (Exception e) {
            return response.internalError("Failed to retrieve user", e);
        }
    }

    /**
     * Updates an existing user.
     * <p>
     * Updates an existing user with the given ID with the provided user object.
     *
     * @param userId the ID of the user to update
     * @param param  the user object to update with
     * @return a response containing the updated user
     */
    @Operation(summary = "Update user", description = "Updates an existing user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to update user"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> updateUser(@Parameter @PathVariable Long userId, @Parameter @RequestBody User param) {
        try {
            UserRegisterAttempt updateAttempt = service.updateUser(param);
            if (updateAttempt != null && !updateAttempt.getSuccess()) {
                return new ApiBuilder<User>().badResponse(updateAttempt.getState().name(), "Failed to update user");
            }

            return new ApiBuilder<User>().success("User updated successfully", param);
        } catch (Exception e) {
            e.printStackTrace();
            return response.internalError("Failed to update user", e);
        }
    }

    /**
     * Enables a user.
     *
     * <p>
     * This operation sets the <code>disabled</code> flag of the user identified by the given
     * <code>userId</code> to <code>false</code>. If the enable operation is successful, a response
     * with a 200 status code is returned. If the user is not found or the enable operation fails,
     * appropriate error responses with status codes 400 or 404 are returned, respectively. In case
     * of an internal server error, a 500 status code is returned.
     *
     * @param userId The ID of the user to enable.
     * @return A ResponseEntity containing the result of the enable operation.
     */
    @Operation(summary = "Enable a user", description = "Enables a user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User enabled successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to enable user"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("{userId}/enable")
    public ResponseEntity<ApiResponse<User>> enableUser(@PathVariable Long userId) {
        try {
            UserRegisterAttempt attempt = service.enableUser(userId);
            if (!attempt.getSuccess()) {
                return new ApiBuilder<User>().badResponse(attempt.getState().name(), "Failed to enable user");
            }

            return new ApiBuilder<User>().success("User enabled successfully", attempt.getUser());
        } catch (Exception e) {
            e.printStackTrace();
            return response.internalError("Failed to enable user", e);
        }
    }

    /**
     * Disables a user by setting its disabled flag to true.
     * <p>
     * This method attempts to disable a user identified by the given userId. If the disable is
     * successful, a response with a 200 status code is returned. If the user is not found or the
     * disable fails, appropriate error responses with status codes 400 or 404 are returned,
     * respectively. In case of an internal server error, a 500 status code is returned.
     *
     * @param userId The ID of the user to be disabled.
     * @return A ResponseEntity containing the result of the disable operation.
     */
    @Operation(summary = "Disable a user", description = "Disables a user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User disabled successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to disable user"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("{userId}/disable")
    public ResponseEntity<ApiResponse<User>> disableUser(@PathVariable Long userId) {
        try {
            UserRegisterAttempt attempt = service.disableUser(userId);
            if (!attempt.getSuccess()) {
                return new ApiBuilder<User>().badResponse(attempt.getState().name(), "Failed to disable user");
            }

            return new ApiBuilder<User>().success("User disabled successfully", attempt.getUser());
        } catch (Exception e) {
            e.printStackTrace();
            return response.internalError("Failed to disable user", e);
        }
    }

    /**
     * Uploads a user image.
     * <p>
     * This method uploads a user image from the given file and sets it as the user's profile image.
     * The image is stored in the user's directory in the storage specified in the application
     * configuration. The image is stored with the same name as the original file. If the image is
     * uploaded successfully, a response with a 200 status code is returned. If the image is not
     * uploaded successfully, appropriate error responses with status codes 400 or 500 are returned,
     * respectively. In case of an internal server error, a 500 status code is returned.
     * <p>
     * The response contains a json object with the following fields:
     * <ul>
     * <li>fileName - the name of the uploaded file</li>
     * <li>errorType - the type of error if the image was not uploaded successfully</li>
     * <li>message - a message describing the result of the upload</li>
     * </ul>
     *
     * @param file   the image to upload
     * @param userId the ID of the user to update
     * @return a ResponseEntity containing the result of the upload operation
     */
    @Operation(summary = "Upload User Image", description = "Uploads a user image")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{userId}/change-image")
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadImage(
            @io.swagger.v3.oas.annotations.parameters.RequestBody() MultipartFile file,
            @PathVariable Long userId
    ) {
        try {
            UserRegisterAttempt attempt = service.updateUserImage(file, userId);

            // map FileUploadError to UserRegisterState
            FileUploadError errorType = attempt.getState() != null ? UserRegisterState.fromFileUploadError(attempt.getState()) : null;
            String message = (errorType == null) ? "Image uploaded successfully" : attempt.getState().name();

            return new ApiBuilder<FileUploadResponse>().success(message, new FileUploadResponse(file.getOriginalFilename(), errorType, message));

        } catch (Exception e) {
            e.printStackTrace();
            return response.internalError("Failed to upload image", e);
        }
    }
}
