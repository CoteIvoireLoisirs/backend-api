/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-28 09:41:34
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-28 09:41:34
 */
package com.erastedev.ciexplore.v1.infrastructure.repository.user;

import com.erastedev.ciexplore.v1.domain.entities.user.UserProfile;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends ICommonRepository<UserProfile> {
    /**
     * Checks if a UserProfile exists by workspace code and user ID.
     *
     * @param workspaceCode the code of the workspace
     * @param userId        the ID of the user
     * @return true if a UserProfile exists with the given workspace code and user ID, false otherwise
     */
    boolean existsByWorkspaceCodeAndUserId(String workspaceCode, Long userId);

    /**
     * Retrieves a list of UserProfiles for the given user ID.
     *
     * @param userId the ID of the user
     * @return a list of UserProfiles
     */
    List<UserProfile> findByUserIdAndDeletedIsNull(Long userId);

    /**
     * Retrieves a UserProfile by workspace code and user ID.
     *
     * @param workspaceCode the code of the workspace
     * @param userId        the ID of the user
     * @return a UserProfile
     */
    UserProfile findByWorkspaceCodeAndUserId(String workspaceCode, Long userId);

    /**
     * Retrieves a list of UserProfiles for the given workspace code.
     *
     * @param workspaceCode the code of the workspace
     * @return a list of UserProfiles
     */
    List<UserProfile> findByWorkspaceCode(String workspaceCode);
}
