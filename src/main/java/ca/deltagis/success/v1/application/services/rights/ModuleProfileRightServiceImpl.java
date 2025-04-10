/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-25 08:45:52
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-25 08:45:52
 */
package ca.deltagis.success.v1.application.services.rights;

import ca.deltagis.success.v1.application.request.right.CreateModuleProfileRightRequest;
import ca.deltagis.success.v1.application.request.right.CreateModuleProfileRightResponse;
import ca.deltagis.success.v1.adapters.web.message.rights.CreateModuleProfileRightError;
import ca.deltagis.success.v1.application.services.rights.profile.ProfileServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserAuthServiceImpl;
import ca.deltagis.success.v1.application.validator.out.RightStringValidatorImpl;
import ca.deltagis.success.v1.domain.core.entities.rights.ModuleProfileRight;
import ca.deltagis.success.v1.domain.core.entities.rights.profil.Profile;
import ca.deltagis.success.v1.domain.core.entities.user.User;
import ca.deltagis.success.v1.domain.core.models.rigths.DefaultSystemRight;
import ca.deltagis.success.v1.domain.core.models.rigths.ModuleProfileRightModel;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
import ca.deltagis.success.v1.domain.ports.in.rights.IModuleProfileRightService;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonService;
import ca.deltagis.success.v1.domain.ports.out.Rights.ModuleRightParser;
import ca.deltagis.success.v1.infrastructure.repository.rights.ModuleProfileRightRepository;
import ca.deltagis.success.v1.infrastructure.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModuleProfileRightServiceImpl extends AbstractCommonService<ModuleProfileRight> implements IModuleProfileRightService {
    @Autowired
    ProfileServiceImpl profileService;

    @Autowired
    UserAuthServiceImpl userAuthService;

    @Autowired
    private ModuleProfileRightRepository repository;

    private Logger logger = LoggerFactory.getLogger(ModuleProfileRightServiceImpl.class);

    @Override
    public List<ModuleProfileRight> findAll() {
        return repository.findAll();
    }

    @Override
    public Long deleteById(Long id) {
        repository.deleteById(id);
        return id;
    }

    @Override
    public boolean existsById(Long id) {
        return repository.findById(id).isPresent();
    }

    @Override
    public ModuleProfileRight save(ModuleProfileRight entity) {
        return repository.save(entity);
    }

    @Override
    public ICommonRepository<ModuleProfileRight> getRepository() {
        return repository;
    }

    /**
     * Retrieves a list of ModuleProfileRight objects, setting the associated Profile
     * and parsing the module rights for each object.
     *
     * <p>This method fetches all ModuleProfileRight entries from the repository where
     * the 'deleted' field is null. It enriches each entry by:
     * <ul>
     *   <li>Setting the associated Profile by fetching it from the ProfileService using the ID.</li>
     *   <li>Parsing the rights string into a structured format and assigning it to the object.</li>
     * </ul>
     *
     * @return a List of enriched ModuleProfileRight objects, where each object contains
     * its associated Profile and parsed module rights.
     */
    public List<ModuleProfileRight> getModuleProfileRightList() {
        profileService.initializeDefaultData();

        return repository.findAllByDeletedIsNull()
                .stream()
                .peek(moduleProfileRight -> {
                    Profile profile = profileService.getByIdOptional(moduleProfileRight.getProfileId()).orElse(null);
                    moduleProfileRight.setProfile(profile);
                    moduleProfileRight.setModuleRight(parseModuleRights(moduleProfileRight.getRights()));
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a ModuleProfileRight object based on the provided id.
     *
     * <p>This method fetches a ModuleProfileRight entry from the repository using the provided ID.
     * It enriches the entry by:
     * <ul>
     *   <li>Setting the associated Profile by fetching it from the ProfileService using the ID.</li>
     *   <li>Parsing the rights string into a structured format and assigning it to the object.</li>
     * </ul>
     *
     * @param id the ID of the ModuleProfileRight object to retrieve.
     * @return the ModuleProfileRight object with the specified id, or null if not found.
     */
    public ModuleProfileRight getModuleProfileRight(Long id) {
        return repository.findById(id)
                .map(moduleProfileRight -> {
                    Profile profile = profileService.getByIdOptional(moduleProfileRight.getProfileId()).orElse(null);
                    moduleProfileRight.setProfile(profile);
                    moduleProfileRight.setModuleRight(parseModuleRights(moduleProfileRight.getRights()));
                    return moduleProfileRight;
                })
                .orElse(null);
    }

    /**
     * Deletes a ModuleProfileRight object with the specified id.
     *
     * <p>This method fetches a ModuleProfileRight entry from the repository using the provided ID.
     * It sets the deleted timestamp of the associated Profile and saves it.
     * If the retrieval fails, the method returns null.
     *
     * @param id The id of the ModuleProfileRight object to delete.
     * @return the ModuleProfileRight object with the specified id, or null if not found.
     */
    public ModuleProfileRight deleteModuleProfileRight(Long id) {
        ModuleProfileRight moduleProfileRight = getModuleProfileRight(id);
        if (moduleProfileRight == null || moduleProfileRight.getDeleted() != null || moduleProfileRight.getProfile().isDefault()) {
            return null;
        }

        // delete profile
        Profile profile = moduleProfileRight.getProfile();
        profile.setDeleted(DateUtil.getCurrentTimestamp());
        profileService.save(profile);

        // delete module profile right
        moduleProfileRight.setDeleted(DateUtil.getCurrentTimestamp());
        return repository.save(moduleProfileRight);
    }

    /**
     * Parses a string of module rights into a structured format.
     *
     * <p>This method takes a string representation of module rights and converts it into
     * a HashMap, where the key is the module name and the value is another HashMap containing
     * rights and their status (enabled or disabled).
     *
     * @param rights The string of module rights to parse.
     * @return A HashMap representing the structured module rights.
     */
    @Override
    public HashMap<String, HashMap<String, Boolean>> parseModuleRights(String rights) {
        return ModuleRightParser.parseModuleRights(rights);
    }

    /**
     * Validates the request and returns a response containing any errors.
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
    @Override
    public CreateModuleProfileRightResponse createProfileWithRight(CreateModuleProfileRightRequest request) {
        CreateModuleProfileRightResponse checkRequest = validateRequest(request);
        if (checkRequest.getError() != null) {
            return checkRequest;
        }

        Profile profile = createProfile(request.getProfile());
        ModuleProfileRight moduleProfileRight_ = createModuleProfileRight(request.getRights(), profile);
        ModuleProfileRight moduleProfileRight = getModuleProfileRight(moduleProfileRight_.getId());

        return new CreateModuleProfileRightResponse(moduleProfileRight, null, "Profile has been created successfully");
    }

    /**
     * Updates a module profile with the specified rights.
     * <p>
     * This method checks if the request is valid and updates a profile with the specified information.
     * If the request is invalid, the method returns a response containing the appropriate error.
     * Otherwise, the method returns a response containing the updated ModuleProfileRight.
     *
     * @return the response containing the updated ModuleProfileRight or an error
     */
    @Override
    public CreateModuleProfileRightResponse updateProfileWithRight(Long id, CreateModuleProfileRightRequest request) {
        CreateModuleProfileRightResponse checkRequest = validateUpdateRequest(request);
        if (checkRequest.getError() != null) {
            return checkRequest;
        }

        ModuleProfileRight moduleProfileRight = getModuleProfileRight(id);
        if (moduleProfileRight == null) {
            return new CreateModuleProfileRightResponse(null, CreateModuleProfileRightError.INVALID_PROFILE, "Profile does not exist");
        }

        Profile profile = createProfile(request.getProfile());
        moduleProfileRight.setProfile(profile);
        moduleProfileRight.setRights(request.getRights());
        moduleProfileRight.setUpdated(DateUtil.getCurrentTimestamp());
        moduleProfileRight.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());
        repository.save(moduleProfileRight);
        ModuleProfileRight updatedModuleProfileRight = getModuleProfileRight(moduleProfileRight.getId());

        return new CreateModuleProfileRightResponse(updatedModuleProfileRight, null, "Profile has been updated successfully");
    }

    /**
     * Deletes a module profile with the specified id.
     * <p>
     * This method checks if the profile exists and deletes it if it does.
     * If the profile does not exist, the method returns a response containing the appropriate error.
     * Otherwise, the method returns a response containing a success message.
     *
     * @param id the id of the profile to delete
     * @return the response containing the success message or an error
     */
    @Override
    public CreateModuleProfileRightResponse deleteProfileWithRight(Long id) {
        if (id == null) {
            return new CreateModuleProfileRightResponse(null, CreateModuleProfileRightError.INVALID_PROFILE, "Profile does not exist");
        }

        ModuleProfileRight delete = deleteModuleProfileRight(id);
        if (delete == null) {
            return new CreateModuleProfileRightResponse(null, CreateModuleProfileRightError.NOT_FOUND, "Profile does not exist");
        }
        return new CreateModuleProfileRightResponse(delete, null, "Profile has been deleted successfully");
    }

    /**
     * Creates a module profile with the specified rights.
     * <p>
     * This method checks if the request is valid and creates a profile with the specified information.
     * If the request is invalid, the method returns a response containing the appropriate error.
     * Otherwise, the method returns a response containing the created ModuleProfileRight.
     *
     * @return the response containing the created ModuleProfileRight or an error
     */
    @Override
    public Profile createProfile(Profile profile) {
        User user = userAuthService.getCurrentLoggedUser();
        profile.setAutoFields();
        profile.setUpdateBy(user.getId());
        return profileService.save(profile);
    }

    /**
     * Creates a module profile with the specified rights.
     * <p>
     * This method creates a module profile with the specified rights.
     * <p>
     * The method takes a rights string and a profile as parameters.
     * The rights string is a string that may contain the letters 'r', 'w', and 'd', which
     * indicate read, write, and delete permissions, respectively.
     * The profile is a domain object that contains information about the profile.
     * <p>
     * The method splits the rights string into individual rights and checks if the
     * profile is valid. If the profile is invalid or the rights string is invalid,
     * the method returns a response containing the appropriate error.
     * Otherwise, the method creates a ModuleProfileRight object with the specified
     * rights and profile, and returns a response containing the created
     * ModuleProfileRight.
     *
     * @return the response containing the created ModuleProfileRight or an error
     */
    @Override
    public boolean isValidRequestProfile(CreateModuleProfileRightRequest request) {
        return request.getProfile() != null;
    }

    /**
     * Checks if the request contains valid rights information.
     * <p>
     * This method validates the rights information in the request by ensuring that the rights
     * string is not null and is valid according to the RightStringValidatorImpl.
     *
     * @param request the request to validate
     * @return true if the rights information is valid, false otherwise
     */
    @Override
    public boolean isValidRequestRight(CreateModuleProfileRightRequest request) {
        RightStringValidatorImpl rightStringValidator = new RightStringValidatorImpl();
        return request.getRights() != null && rightStringValidator.isValid(request.getRights());
    }

    /**
     * Validates a CreateModuleProfileRightRequest.
     *
     * @param request The request to validate.
     * @return A CreateModuleProfileRightResponse indicating the validation result.  If valid, the error field will be null.
     * Otherwise, the response will contain an appropriate error code and message.
     */
    @Override
    public CreateModuleProfileRightResponse validateRequest(CreateModuleProfileRightRequest request) {
        if (!isValidRequestProfile(request)) {
            return new CreateModuleProfileRightResponse(null, CreateModuleProfileRightError.INVALID_PROFILE, "Profile is null or invalid");
        }
        if (!isValidRequestRight(request)) {
            return new CreateModuleProfileRightResponse(null, CreateModuleProfileRightError.INVALID_RIGHT_STRING, "Right is null or invalid");
        }
        return new CreateModuleProfileRightResponse(null, null, "Request is valid");
    }

    @Override
    public CreateModuleProfileRightResponse validateUpdateRequest(CreateModuleProfileRightRequest request) {
        // check id
        if (request.getId() == null) {
            return new CreateModuleProfileRightResponse(null, CreateModuleProfileRightError.ID_REQUIRED, "Id is required");
        }

        // check profile id
        if (request.getProfile() == null || request.getProfile().getId() == null) {
            return new CreateModuleProfileRightResponse(null, CreateModuleProfileRightError.INVALID_PROFILE, "Profile is required");
        }

        return validateRequest(request);
    }

    /**
     * Creates a ModuleProfileRight entity and persists it to the database.
     *
     * @param rights  The rights string associated with the module profile right.
     * @param profile The profile associated with the module profile right.
     * @return The created and saved ModuleProfileRight entity.
     */
    @Override
    public ModuleProfileRight createModuleProfileRight(String rights, Profile profile) {
        ModuleProfileRight moduleProfileRight = new ModuleProfileRight();
        moduleProfileRight.setRights(rights);
        moduleProfileRight.setProfileId(profile.getId());
        moduleProfileRight.setUpdateBy(userAuthService.getLoggedUserId());
        moduleProfileRight.setAutoFields();
        return repository.save(moduleProfileRight);
    }

    /**
     * Creates default module rights if none exist.
     *
     * <p>This method checks if there are any existing ModuleProfileRights with a non-deleted status.
     * If none exist, it iterates over all default system rights, retrieves the corresponding rights string,
     * and creates a Profile for each default right. These profiles are then associated with the default rights
     * to create ModuleProfileRight entries in the repository.
     */
    @Override
    public void createDefaultModuleRight() { // Removed unused parameter
        logger.info("start createDefaultModuleRight ...");

        if (repository.countByDeletedIsNull() == 0) { // More efficient empty check
            DefaultSystemRight.getAll().forEach(defaultRight -> { // Use forEach for side effects
                String moduleRightString = ModuleProfileRightModel.getProfileRights(defaultRight);
                Profile profile = profileService.getByNameAndIsDefault(defaultRight.toString());
                if (profile != null) { // Null check
                    createModuleProfileRight(moduleRightString, profile);
                } else {
                    logger.warn("Profile not found for DefaultSystemRight: {}", defaultRight); // Log the missing profile
                }
            });
        }

        logger.info("end createDefaultModuleRight ...");
    }

    @Override
    public ModuleProfileRight getDefaultRightByName(DefaultSystemRight name) {
        try {
            Profile profile = profileService.getByNameAndIsDefault(name.toString());

            if (profile != null) {
                ModuleProfileRight moduleProfileRight = repository.findByProfileIdAndDeletedIsNull(profile.getId());
                return getModuleProfileRight(moduleProfileRight.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
