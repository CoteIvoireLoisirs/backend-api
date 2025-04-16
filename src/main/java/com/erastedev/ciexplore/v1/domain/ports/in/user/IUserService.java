package com.erastedev.ciexplore.v1.domain.ports.in.user;


import com.erastedev.ciexplore.v1.application.validator.in.CommonValidation;
import com.erastedev.ciexplore.v1.application.validator.in.CommonError;
import com.erastedev.ciexplore.v1.domain.entities.user.model.User;
import com.erastedev.ciexplore.v1.domain.models.FileUploadResponse;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserDeleteResponse;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserRegisterAttempt;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to find
     * @return the user associated with the given username, or null if no such user exists
     */
    User getUserByUsername(String username);

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to find
     * @return the user associated with the given email address, or null if no such user exists
     */
    User getUserByEmail(String email);

    /**
     * Retrieves a user by their email address or username.
     *
     * @param login the email address or username of the user to find
     * @return an Optional containing the user associated with the given email address or username,
     * or an empty Optional if no such user exists
     */
    Optional<User> getOptionalUserByEmailOrUsername(String login);

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to find
     * @return an Optional containing the user associated with the given email address, or an empty Optional if no such user exists
     */
    Optional<User> getOptionalUserByEmail(String email);

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the user associated with the given username, or an empty Optional if no such user exists
     */
    Optional<User> getOptionalUserByUsername(String username);

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to find
     * @return an Optional containing the user associated with the given ID, or an empty Optional if no such user exists
     */
    Optional<User> getOptionalUserById(Long id);

    /**
     * Creates a new user from the provided user details.
     *
     * @param user the user details to create the user from
     * @return the user that was created
     */
    UserRegisterAttempt createUserFromWorkspace(User user, String workspaceCode);

    /**
     * Creates a new user without associating it to any workspace.
     *
     * @param user the user details to create the user from
     * @return the user that was created
     */
    UserRegisterAttempt createUserWithoutAssociation(User user);

    /**
     * Updates the provided user details.
     *
     * @param user the user details to update
     * @return the result of the update
     */
    UserRegisterAttempt updateUser(User user);

    /**
     * Enables or disables a user.
     *
     * @param user   the user to enable or disable
     * @param enabled whether to enable or disable the user
     * @return the user that was enabled or disabled
     */
    User enableOrDisableUser(User user, boolean enabled);

    /**
     * Updates the profile image of a user.
     *
     * @param file   the profile image to upload
     * @param userId the ID of the user to update
     * @return the result of the update
     */
    UserRegisterAttempt updateUserImage(MultipartFile file, Long userId);

    /**
     * Enables a user.
     *
     * @param userId the ID of the user to enable
     * @return a UserRegisterAttempt containing the result of the enable
     */
    UserRegisterAttempt enableUser(Long userId);

    /**
     * Disables a user.
     *
     * @param userId the ID of the user to disable
     * @return a UserRegisterAttempt containing the result of the disable operation
     */
    UserRegisterAttempt disableUser(Long userId);

    /**
     * Updates the profile image of a user.
     *
     * @param file the profile image to upload
     * @param user the user to update
     * @return a FileUploadResponse containing the result of the update
     */
    FileUploadResponse changeUserImage(MultipartFile file, User user);

    /**
     * Deletes a user.
     *
     * @param id the ID of the user to delete
     * @return a UserDeleteResponse containing the result of the deletion
     */
    UserDeleteResponse deleteUser(Long id);

    /**
     * Retrieves all users from the repository.
     *
     * @return a list of all users from the repository, or a validation error if an error occurs
     */
    CommonValidation<List<User>, CommonError> getAllUsers(String workspaceCode);

    /**
     * Sorts the given list of users by their access to the given workspace.
     * <p>
     * This method will sort the given list of users by their access to the given workspace. Users with access to the workspace will be placed before users without access.
     *
     * @param userList      the list of users to sort
     * @param workspaceCode the code of the workspace to filter by
     * @return a sorted list of users
     */
    List<User> withUserProfile(List<User> userList, String workspaceCode);

    /**
     * Sorts the given list of users by their access to the given workspace.
     * <p>
     * This method will sort the given list of users by their access to the given workspace. Users with access to the workspace will be placed before users without access.
     *
     * @param users         the list of users to sort
     * @param workspaceCode the code of the workspace to filter by
     * @return a sorted list of users
     */
    List<User> sortUsersByWorkspaceAccess(List<User> users, String workspaceCode);

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to find
     * @return the user associated with the given ID, or null if no such user exists
     */
    User getUser(Long id);

    /**
     * Encodes the provided password using a password encoder.
     *
     * @param password the password to encode
     * @return a String instance with the encoded password
     */
    String encodePassword(String password);
}