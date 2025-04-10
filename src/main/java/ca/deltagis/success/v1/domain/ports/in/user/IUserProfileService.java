/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-28 09:42:52
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-28 09:42:52
 */
package ca.deltagis.success.v1.domain.ports.in.user;

import ca.deltagis.success.v1.application.request.user.AssociateUserWorkspaceRequest;
import ca.deltagis.success.v1.application.request.user.SaveUserProfileResponse;
import ca.deltagis.success.v1.domain.core.entities.user.User;
import ca.deltagis.success.v1.domain.core.entities.user.UserProfile;
import ca.deltagis.success.v1.domain.core.models.rigths.DefaultSystemRight;

import java.util.List;

public interface IUserProfileService {
    /**
     * Saves or updates the user profile.
     *
     * <p>
     * This method will save the user profile if it does not already exist, or update
     * it if it does exist. The method will return the saved or updated user profile
     * object.
     * </p>
     *
     * @param entity the user profile to save or update
     * @return the saved or updated user profile
     */
    UserProfile saveOrUpdate(UserProfile entity);

    /**
     * Retrieves a list of all user profiles.
     *
     * @return a list of all user profiles.
     */
    public UserProfile withRight(UserProfile userProfile);

    /**
     * This interface defines the contract that must be implemented by any service class
     * that will be responsible for managing user profiles.
     */
    List<UserProfile> getUserProfiles(int userId);

    /**
     * Retrieves a list of all user profiles for a given user ID.
     *
     * @param userId the ID of the user.
     * @return a list of UserProfile objects.
     */
    List<UserProfile> getAllByWorkspaceCodeAndUserId(String workspaceCode, Long userId);

    /**
     * Retrieves a user profile by user ID and workspace code.
     *
     * @param workspaceCode the code of the workspace.
     * @param userId        the ID of the user.
     * @return the user profile.
     */
    UserProfile getByWorkspaceCodeAndUserId(String workspaceCode, Long userId);

    /**
     * Retrieves a list of all user profiles for a given workspace code.
     *
     * @param workspaceCode the code of the workspace.
     * @return a list of UserProfile objects.
     */
    List<UserProfile> getByWorkspaceCode(String workspaceCode);

    /**
     * Checks if the given user has administrative rights.
     *
     * @param user the user to check.
     * @return true if the user is an admin, false otherwise.
     */
    boolean userIsAdmin(User user);

    /**
     * Defines and creates user profiles for a list of workspaces.
     *
     * @param params List of UserProfile objects representing the user profiles to be created for the workspaces.
     * @return SaveUserProfileResponse containing the result of the operation, including any validation errors.
     */
    SaveUserProfileResponse associateUserToWorkspace(AssociateUserWorkspaceRequest params);

    /**
     * Checks if a user profile exists for a given workspace code and user ID.
     *
     * @param userProfile the user profile.
     * @return true if the user profile exists, false otherwise.
     */
    boolean existByUserProfile(UserProfile userProfile);

    /**
     * Checks if a user profile exists for a given workspace code and user ID.
     *
     * @param workspaceCode the code of the workspace.
     * @param userId        the ID of the user.
     * @return true if the user profile exists, false otherwise.
     * @throws IllegalArgumentException if workspaceCode or userId is null.
     */
    boolean existsByWorkspaceCodeAndUserId(String workspaceCode, Long userId);

    /**
     * Associates a user to a profile.
     *
     * @param user          the user to associate.
     * @param workspaceCode the code of the workspace.
     * @param right         the right to associate.
     * @return the associated user profile.
     */
    UserProfile associateUserToProfile(User user, String workspaceCode, DefaultSystemRight right);

    /**
     * Deletes all user profiles associated with the specified user ID.
     *
     * @param userId the ID of the user whose profiles are to be deleted
     */
    void deleteAllUserProfileByUserId(Long userId);

    /**
     * Converts an AssociateUserWorkspaceRequest into a UserProfile.
     *
     * @param param The request containing user workspace association details.
     * @return A UserProfile object created from the given request.
     */
    UserProfile userProfileFromRequest(AssociateUserWorkspaceRequest param);
}
