/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-10 11:37:57
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-11 16:51:27
 * @FilePath: src/main/java/ca/deltagis/success/v1/application/validator/out/user/CreateUserValidator.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package com.erastedev.ciexplore.v1.application.validator.out.user;

import com.erastedev.ciexplore.v1.application.request.user.SignUpRequest;
import com.erastedev.ciexplore.v1.application.services.user.UserServiceImpl;
import com.erastedev.ciexplore.v1.application.services.workspace.WorkspaceServiceImpl;
import com.erastedev.ciexplore.v1.domain.entities.user.model.User;
import com.erastedev.ciexplore.v1.domain.entities.workspace.WorkSpaceStatus;
import com.erastedev.ciexplore.v1.domain.entities.workspace.Workspace;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserDeleteResponse;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserRegisterAttempt;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserRegisterState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CreateUserValidator {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    WorkspaceServiceImpl workspaceService;

    private Logger logger = LoggerFactory.getLogger(CreateUserValidator.class);

    /**
     * return UserRegisterAttempt when error occur during user validation
     *
     * @param user to validate
     * @return UserRegisterAttempt
     */
    public UserRegisterAttempt createUserValidation(User user) {
        try {
            if (user == null || user.getEmail() == null || user.getUsername() == null) {
                return UserRegisterAttempt.builder().success(false).state(UserRegisterState.USER_NOT_FOUND).build();
            }

            if (user.getPassword() == null) {
                return UserRegisterAttempt.builder().success(false).state(UserRegisterState.PASSWORD_NOT_FOUND).build();
            }

            User checkUsername = userService.getUserByUsername(user.getUsername());
            if (checkUsername != null) {
                return UserRegisterAttempt.builder().success(false).state(UserRegisterState.USERNAME_ALREADY_USED).build();
            }

            User checkEmail = userService.getUserByEmail(user.getEmail());
            if (checkEmail != null) {
                return UserRegisterAttempt.builder().success(false).state(UserRegisterState.EMAIL_ALREADY_USED).build();
            }

            return null;
        } catch (Exception e) {
            logger.error("userValidation {}", e.getMessage());
            return UserRegisterAttempt.builder().success(false).state(UserRegisterState.SOMETHING_WENT_WRONG).build();
        }
    }

    /**
     * Validates the registration attempt based on the provided sign-up request.
     * Checks if the required fields are present and whether the email or username
     * are already in use. If validation fails, returns a {@link UserRegisterAttempt}
     * with an appropriate failure state. If an error occurs during validation, returns
     * a {@link UserRegisterAttempt} with a general failure state.
     *
     * @param signUpRequest the sign-up request containing user registration details to validate
     * @return a {@link UserRegisterAttempt} indicating the result of the validation attempt
     */
    public UserRegisterAttempt validateRegisterAttempt(SignUpRequest signUpRequest) {
        try {
            if (signUpRequest == null || signUpRequest.getEmail() == null || signUpRequest.getUsername() == null) {
                return UserRegisterAttempt.builder().success(false).state(UserRegisterState.USER_NOT_FOUND).build();
            }

            if (signUpRequest.getPassword() == null) {
                return UserRegisterAttempt.builder().success(false).state(UserRegisterState.PASSWORD_NOT_FOUND).build();
            }

            User checkUsername = userService.getUserByUsername(signUpRequest.getUsername());
            if (checkUsername != null) {
                return UserRegisterAttempt.builder().success(false).state(UserRegisterState.USERNAME_ALREADY_USED).build();
            }

            User checkEmail = userService.getUserByEmail(signUpRequest.getEmail());
            if (checkEmail != null) {
                return UserRegisterAttempt.builder().success(false).state(UserRegisterState.EMAIL_ALREADY_USED).build();
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return UserRegisterAttempt.builder().success(false).state(UserRegisterState.SOMETHING_WENT_WRONG).build();
        }
    }

    /**
     * return UserRegisterAttempt when error occur during user validation
     * <p>
     * This function validate the user to update, and check if the user
     * exist in the database, and check if the user try to update the email
     * or the username.
     *
     * @param user the user to validate
     * @return UserRegisterAttempt
     */
    public UserRegisterAttempt updateUserValidation(User user) {
        try {
            // user data not be null
            if (user == null || user.getId() == null) {
                return UserRegisterAttempt.builder().success(false).state(UserRegisterState.USER_NOT_FOUND).build();
            }

            // can't update username and email because violation unique
            // if (user.getEmail() != null || user.getUsername() != null) {
            Optional<User> userOldData = userService.getOptionalUserById(user.getId());
            if (userOldData.isPresent()) {
                // can't update username
                if (!Objects.equals(userOldData.get().getEmail(), user.getEmail())) {
                    return UserRegisterAttempt.builder().success(false).state(UserRegisterState.UPDATE_EMAIL_DENIED).build();
                } else if (!Objects.equals(userOldData.get().getUsername(), user.getUsername())) {
                    return UserRegisterAttempt.builder().success(false).state(UserRegisterState.UPDATE_USERNAME_DENIED).build();
                }

                // user deleted
                if (userOldData.get().getDeleted() != null) {
                    return UserRegisterAttempt.builder().success(false).state(UserRegisterState.USER_NOT_FOUND).build();
                }
            }
            //}

            // all right
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return UserRegisterAttempt.builder().success(false).state(UserRegisterState.SOMETHING_WENT_WRONG).build();
        }
    }

    /**
     * return UserRegisterAttempt when error occur during enable or disable user validation
     * <p>
     * This function validate the user to enable or disable, and check if the user
     * exist in the database, and check if the user is already deleted.
     *
     * @param user the user to validate
     * @return UserRegisterAttempt
     */
    public UserRegisterAttempt enableOrDisableUserValidation(User user) {
        if (user == null || user.getId() == null) {
            return UserRegisterAttempt.builder().success(false).state(UserRegisterState.USER_NOT_FOUND).build();
        }

        Optional<User> userOldData = userService.getOptionalUserById(user.getId());
        if (userOldData.isPresent() && userOldData.get().getDeleted() != null) {
            return UserRegisterAttempt.builder().success(false).state(UserRegisterState.USER_NOT_FOUND).build();
        }

        return null;
    }

    /**
     * return UserRegisterAttempt when error occur during user validation
     *
     * @param workspace to validate
     * @return UserRegisterAttempt
     */
    public UserRegisterAttempt workspaceValidation(Workspace workspace) {
        try {
            if (workspace == null || workspace.getCode() == null) {
                return UserRegisterAttempt.builder().success(false).state(UserRegisterState.WORKSPACE_NOT_FOUND).build();
            } else if (workspace.getStatus() == WorkSpaceStatus.DELETED || workspace.getStatus() == WorkSpaceStatus.DISABLE) {
                return UserRegisterAttempt.builder().success(false).state(UserRegisterState.WORKSPACE_IS_NOT_ACTIVE).build();
            }

            return null;
        } catch (Exception e) {
            logger.error("workspaceValidation {}", e.getMessage());
            return UserRegisterAttempt.builder().success(false).state(UserRegisterState.SOMETHING_WENT_WRONG).build();
        }
    }

    /**
     * return UserRegisterAttempt when error occur during workspace validation
     *
     * @param workspace to validate
     * @return UserRegisterAttempt
     */
    public UserRegisterAttempt validateCreateUserAndAssociateToWorkspace(User user, Workspace workspace) {
        try {
            UserRegisterAttempt userValidation = createUserValidation(user);
            if (userValidation != null) {
                return userValidation;
            }

            return workspaceValidation(workspace);
        } catch (Exception e) {
            logger.error("validateCreateUser {}", e.getMessage());
            return UserRegisterAttempt.builder().success(false).state(UserRegisterState.SOMETHING_WENT_WRONG).build();
        }
    }

    /**
     * Deletes a user from the workspace.
     * <p>
     * This operation will delete a user from the workspace.
     *
     * @param userId        the ID of the user to delete.
     * @param workspaceCode the code of the workspace to delete from.
     * @return a response containing the result of the deletion.
     */
    public UserDeleteResponse deleteUser(Long userId, String workspaceCode) {
        try {
            // check user
            User user = userService.getUser(userId);
            if (user == null) {
                return UserDeleteResponse.builder().success(false).state(UserRegisterState.USER_NOT_FOUND).build();
            }

            // check workspace
            UserRegisterAttempt _workspaceValidation = workspaceValidation(workspaceService.getByCode(workspaceCode).orElse(null));

            if (_workspaceValidation == null) {
                // check user
                if (user.getId() == null) {
                    return UserDeleteResponse.builder().success(false).state(UserRegisterState.USER_NOT_FOUND).build();
                }
            }

            return null;
        } catch (Exception e) {
            logger.error("deleteUser {}", e.getMessage());
            return UserDeleteResponse.builder().success(false).state(UserRegisterState.SOMETHING_WENT_WRONG).build();
        }
    }
}
