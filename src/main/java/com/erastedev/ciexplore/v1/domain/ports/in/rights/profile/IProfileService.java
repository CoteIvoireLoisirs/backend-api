/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-12 10:37:49
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-12 10:37:49
 */
package com.erastedev.ciexplore.v1.domain.ports.in.rights.profile;

import com.erastedev.ciexplore.v1.domain.entities.rights.profil.Profile;

public interface IProfileService {
    /**
     * The interface for profile service.
     */
    void initializeDefaultData();

    /**
     * Retrieves all profiles.
     *
     * @return a list of all profiles in the database.
     */
    void createDefaultProfile();

    /**
     * Retrieves a profile by its name and if it is default.
     *
     * @param name the name of the profile
     * @return the profile if found, null otherwise
     */
    Profile getByNameAndIsDefault(String name);
}
