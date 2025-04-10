/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-25 08:45:09
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-25 08:45:09
 */
package com.erastedev.ciexplore.v1.domain.ports.in.rights;

import com.erastedev.ciexplore.v1.application.request.right.CreateModuleProfileRightRequest;
import com.erastedev.ciexplore.v1.application.request.right.CreateModuleProfileRightResponse;
import com.erastedev.ciexplore.v1.domain.entities.rights.ModuleProfileRight;
import com.erastedev.ciexplore.v1.domain.entities.rights.profil.Profile;
import com.erastedev.ciexplore.v1.domain.models.rigths.DefaultSystemRight;

import java.util.HashMap;
import java.util.List;

public interface IModuleProfileRightService {
    /**
     * This method retrieves a ModuleProfileRight object based on the provided id.
     *
     * @param id The id of the ModuleProfileRight object to retrieve.
     * @return The ModuleProfileRight object with the specified id, or null if not found.
     */
    ModuleProfileRight getModuleProfileRight(Long id);

    /**
     * This method retrieves a list of all ModuleProfileRight objects.
     *
     * @return A list of all ModuleProfileRight objects.
     */
    List<ModuleProfileRight> getModuleProfileRightList();

    /**
     * This method retrieves a ModuleProfileRight object based on the provided id.
     *
     * @param id The id of the ModuleProfileRight object to retrieve.
     * @return The ModuleProfileRight object with the specified id, or null if not found.
     */
    ModuleProfileRight deleteModuleProfileRight(Long id);

    /**
     * This method parses a string of module rights and returns a HashMap representing the module rights.
     * The HashMap has the module name as the key and another HashMap as the value.
     * The inner HashMap has the right name as the key and a boolean indicating if the right is enabled as the value.
     *
     * @param rights The string of module rights to parse.
     * @return A HashMap representing the module rights.
     */
    HashMap<String, HashMap<String, Boolean>> parseModuleRights(String rights);

    /**
     * Creates a module profile with the specified rights.
     *
     * @param request the request containing profile and rights information
     * @return the response containing the created ModuleProfileRight or an error
     */
    CreateModuleProfileRightResponse createProfileWithRight(CreateModuleProfileRightRequest request);

    /**
     * Updates a module profile with the specified rights.
     *
     * @param id      the id of the module profile to update
     * @param request the request containing profile and rights information
     * @return the response containing the updated ModuleProfileRight or an error
     */
    CreateModuleProfileRightResponse updateProfileWithRight(Long id, CreateModuleProfileRightRequest request);

    /**
     * Deletes a module profile with the specified rights.
     *
     * @param id The id of the ModuleProfileRight object to delete.
     * @return the response containing the deleted ModuleProfileRight or an error
     */
    CreateModuleProfileRightResponse deleteProfileWithRight(Long id);

    /**
     * Creates a profile with the specified information.
     *
     * @param profile the profile information
     * @return the created Profile
     */
    Profile createProfile(Profile profile);

    /**
     * Checks if the request contains valid information.
     *
     * @param request the request to validate
     * @return true if the request is valid, false otherwise
     */
    boolean isValidRequestProfile(CreateModuleProfileRightRequest request);

    /**
     * Checks if the request contains valid rights information.
     *
     * @param request the request to validate
     * @return true if the request is valid, false otherwise
     */
    boolean isValidRequestRight(CreateModuleProfileRightRequest request);

    /**
     * Validates the request and returns a response containing the ModuleProfileRight or an error.
     *
     * @param request the request to validate
     * @return the response containing the ModuleProfileRight or an error
     */
    CreateModuleProfileRightResponse validateRequest(CreateModuleProfileRightRequest request);

    /**
     * Validates the request and returns a response containing the ModuleProfileRight or an error.
     * <p>
     * This method performs the following checks:
     * <ul>
     * <li>Checks if the request is null</li>
     * <li>Checks if the profile is null or invalid</li>
     * <li>Checks if the rights string is null or invalid</li>
     * </ul>
     * <p>
     * If any of the checks fail, the method returns a response containing the appropriate error.
     * Otherwise, the method returns a response with no error.
     *
     * @param request the request to validate
     * @return a response containing any errors
     */
    CreateModuleProfileRightResponse validateUpdateRequest(CreateModuleProfileRightRequest request);

    /**
     * Creates a ModuleProfileRight with the specified rights and profile.
     *
     * @param rights  the rights string
     * @param profile the profile to associate with the rights
     * @return the created ModuleProfileRight
     */
    ModuleProfileRight createModuleProfileRight(String rights, Profile profile);

    /**
     * Creates the default module rights when the application starts.
     *
     * <p>This method is called at application startup and creates the default module rights.
     */
    void createDefaultModuleRight();

    /**
     * Retrieves a ModuleProfileRight with the specified name.
     * <p>
     * This method retrieves a ModuleProfileRight with the specified name.
     * If the ModuleProfileRight does not exist, the method returns null.
     *
     * @param name the name of the ModuleProfileRight to retrieve
     * @return the ModuleProfileRight with the specified name, or null if not found
     */
    ModuleProfileRight getDefaultRightByName(DefaultSystemRight name);
}
