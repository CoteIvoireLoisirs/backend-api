/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-28 09:43:35
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-28 09:43:35
 */
package ca.deltagis.success.v1.application.services.user;

import ca.deltagis.success.v1.application.request.user.AssociateUserWorkspaceRequest;
import ca.deltagis.success.v1.application.request.user.SaveUserProfileResponse;
import ca.deltagis.success.v1.adapters.web.message.user.UserProfileError;
import ca.deltagis.success.v1.application.services.rights.ModuleProfileRightServiceImpl;
import ca.deltagis.success.v1.application.services.rights.RightServiceImpl;
import ca.deltagis.success.v1.application.services.rights.profile.ProfileServiceImpl;
import ca.deltagis.success.v1.application.services.workspace.WorkspaceServiceImpl;
import ca.deltagis.success.v1.application.validator.out.AssociateUserWorkspaceValidation;
import ca.deltagis.success.v1.domain.core.entities.rights.ModuleProfileRight;
import ca.deltagis.success.v1.domain.core.entities.rights.Right;
import ca.deltagis.success.v1.domain.core.entities.rights.profil.Profile;
import ca.deltagis.success.v1.domain.core.entities.user.User;
import ca.deltagis.success.v1.domain.core.entities.user.UserProfile;
import ca.deltagis.success.v1.domain.core.models.rigths.DefaultSystemRight;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
import ca.deltagis.success.v1.domain.ports.in.user.IUserProfileService;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonService;
import ca.deltagis.success.v1.domain.ports.out.Rights.ModuleRightParser;
import ca.deltagis.success.v1.infrastructure.repository.user.UserProfileRepository;
import ca.deltagis.success.v1.infrastructure.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserProfileServiceImpl extends AbstractCommonService<UserProfile> implements IUserProfileService {
    @Autowired
    private UserProfileRepository repository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserAuthServiceImpl userAuthService;

    @Autowired
    private WorkspaceServiceImpl workspaceService;

    @Autowired
    private ModuleProfileRightServiceImpl moduleProfileRightService;

    @Autowired
    private RightServiceImpl rightService;

    @Autowired
    private ProfileServiceImpl profileService;

    @Autowired
    private AssociateUserWorkspaceValidation validator;

    private Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    @Override
    public List<UserProfile> findAll() {
        return repository.findAll();
    }

    @Override
    public Long deleteById(Long id) {
        UserProfile userProfile = repository.findById(id).orElse(null);
        if (userProfile != null) {
            userProfile.setDeleted(DateUtil.getCurrentTimestamp());
            userProfile.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());
            // repository.deleteById(id);
            repository.save(userProfile);
            return id;
        }
        return null;
    }

    @Override
    public boolean existsById(Long id) {
        return repository.findById(id).isPresent();
    }

    @Override
    public UserProfile save(UserProfile entity) {
        entity.setAutoFields();
        entity.setUpdateBy(userAuthService.getLoggedUserId());
        return repository.save(entity);
    }

    @Override
    public UserProfile saveOrUpdate(UserProfile entity) {
        UserProfile entityCheck = repository.findByWorkspaceCodeAndUserId(entity.getWorkspaceCode(), entity.getUserId());
        if (entityCheck != null) {
            repository.delete(entityCheck);
        }

        entity.setAutoFields();
        entity.setUpdateBy(userAuthService.getLoggedUserId());
        return repository.save(entity);
    }

    @Override
    public String delete(UserProfile userProfile) {
        userProfile.setDeleted(DateUtil.getCurrentTimestamp());
        userProfile.setUpdateBy(userAuthService.getLoggedUserId());
        repository.save(userProfile);
        return "success";
    }

    @Override
    public ICommonRepository<UserProfile> getRepository() {
        return repository;
    }

    @Override
    public List<UserProfile> getAll() {
        return getRepository()
                .findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .toList();
    }

    /**
     * Retrieves a list of all user profiles.
     *
     * @return a list of all user profiles.
     */
    @Override
    public UserProfile withRight(UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        }

        userProfile.setRight(moduleProfileRightService.getByIdOptional(userProfile.getModuleProfileRightId()).orElse(null));
        if (userProfile.getRight() != null) {
            userProfile.getRight().setModuleRight(ModuleRightParser.parseModuleRights(userProfile.getRight().getDisplayName()));
            Profile profile = profileService.getByIdOptional(userProfile.getRight().getProfileId()).orElse(null);
            userProfile.getRight().setProfile(profile);
        }

        return userProfile;
    }

    /**
     * Retrieves a list of all user profiles.
     *
     * @return a list of UserProfile objects.
     */
    @Override
    public List<UserProfile> getUserProfiles(int userId) {
        try {
            return repository.findByUserIdAndDeletedIsNull((long) userId)
                    .stream()
                    .peek(this::withRight)
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves a list of UserProfiles for the given user ID, filtered by
     * workspace code.
     *
     * @param workspaceCode the code of the workspace to filter by
     * @param userId        the ID of the user to filter by
     * @return a list of UserProfiles
     */
    @Override
    public List<UserProfile> getAllByWorkspaceCodeAndUserId(String workspaceCode, Long userId) {
        try {
            Optional<User> user = userService.getOptionalUserById(userId);

            if (user.isPresent()) {
                // if user is ADMIN return all rights
                if (userIsAdmin(user.get())) {
                    return repository.findByUserIdAndDeletedIsNull(userId).stream().peek(this::withRight).toList();
                } else {
                    // else return right of workspace
                    UserProfile userProfile = repository.findByWorkspaceCodeAndUserId(workspaceCode, userId);
                    if (userProfile != null) {
                        return List.of(withRight(userProfile));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Failed to retrieve user profiles for user ID {}: {}", userId, e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public UserProfile getByWorkspaceCodeAndUserId(String workspaceCode, Long userId) {
        try {
            UserProfile userProfile = repository.findByWorkspaceCodeAndUserId(workspaceCode, userId);
            if (userProfile != null) {
                return withRight(userProfile);
            }

            User user = userService.getOptionalUserById(userId).orElse(null);
            if (user != null && user.isAdmin()) {
                return adminUserProfile(workspaceCode, userId);
            }

        } catch (Exception e) {
            logger.error("Exception occurred while retrieving user profile for user ID {} and workspace code {}", userId, workspaceCode, e);
        }
        return null;
    }

    /**
     * Creates an administrative user profile for a given workspace code and user ID.
     *
     * <p>This method initializes a new UserProfile object with the provided workspace
     * code and user ID. It assigns the SUPER_ADMIN rights to the user profile by
     * retrieving the default rights for the SUPER_ADMIN role. The profile is then
     * enriched with these rights before being returned.
     *
     * @param workspaceCode the code of the workspace for which the admin profile is created.
     * @param userId        the ID of the user for whom the admin profile is created.
     * @return a UserProfile object with administrative rights for the specified workspace.
     */
    private UserProfile adminUserProfile(String workspaceCode, Long userId) {
        UserProfile admin = new UserProfile();

        admin.setWorkspaceCode(workspaceCode);
        admin.setUserId(userId);

        ModuleProfileRight superAdminRight = moduleProfileRightService.getDefaultRightByName(DefaultSystemRight.SUPER_ADMIN);
        admin.setRight(superAdminRight);
        admin.setModuleProfileRightId(superAdminRight.getId());
        return withRight(admin);
    }

    @Override
    public List<UserProfile> getByWorkspaceCode(String workspaceCode) {
        try {
            return repository.findByWorkspaceCode(workspaceCode).stream().peek(this::withRight).toList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Checks if the given user has the SUPER_ADMIN role.
     *
     * @param user the user to check
     * @return true if the user is a SUPER_ADMIN, false otherwise
     */
    @Override
    public boolean userIsAdmin(User user) {
        if (user != null) {
            // Right right = rightService.getByIdOptional(user.getRoleId()).orElse(null);
            Right right = null;
            if (right != null) {
                return Objects.equals(right.getName(), DefaultSystemRight.SUPER_ADMIN.name());
            }
        }

        return false;
    }

    @Override
    public UserProfile userProfileFromRequest(AssociateUserWorkspaceRequest param) {
        UserProfile profile = new UserProfile();
        profile.setUserId(param.getUserId());
        profile.setModuleProfileRightId(param.getModuleProfileRightId());
        profile.setWorkspaceCode(param.getWorkspaceCode());
        profile.setUpdateBy(userAuthService.getLoggedUserId());
        profile.setAutoFields();
        return profile;
    }

    @Override
    public SaveUserProfileResponse associateUserToWorkspace(AssociateUserWorkspaceRequest params) {
        try {
            SaveUserProfileResponse validation = validator.associateUserToWorkspaceValidation(params);

            logger.info("Validation response: {}", validation);

            if (validation != null) {
                if (validation.getErrors() != null) {
                    logger.error("Validation errors: {}", validation.getErrors().toString());
                }
                return validation;
            }

            List<UserProfile> listSuccess = new ArrayList<>();

            UserProfile savedUserProfile = saveOrUpdate(userProfileFromRequest(params));
            listSuccess.add(savedUserProfile);

            return new SaveUserProfileResponse(listSuccess, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new SaveUserProfileResponse(null, UserProfileError.SOMETHING_WENT_WRONG, null);
        }
    }


    /**
     * Checks if a user profile exists for a given workspace code and user ID.
     *
     * @param userProfile the code of the workspace.
     * @return true if the user profile exists, false otherwise.
     * @throws IllegalArgumentException if workspaceCode or userId is null.
     */
    @Override
    public boolean existByUserProfile(UserProfile userProfile) {
        if (userProfile.getWorkspaceCode() == null || userProfile.getUserId() == null) {
            return false;
        }

        if (userProfile.getId() != null) {
            return false;
        }

        return existsByWorkspaceCodeAndUserId(userProfile.getWorkspaceCode(), userProfile.getUserId());
    }

    /**
     * Checks if a user profile exists for a given workspace code and user ID.
     *
     * @param workspaceCode the code of the workspace.
     * @param userId        the ID of the user.
     * @return true if the user profile exists, false otherwise.
     */
    @Override
    public boolean existsByWorkspaceCodeAndUserId(String workspaceCode, Long userId) {
        return repository.existsByWorkspaceCodeAndUserId(workspaceCode, userId);
    }

    @Override
    public UserProfile associateUserToProfile(User user, String workspaceCode, DefaultSystemRight right) {
        try {
            ModuleProfileRight moduleProfileRight = moduleProfileRightService.getDefaultRightByName(right);
            if (moduleProfileRight == null) {
                // TODO : implement error
            }

            AssociateUserWorkspaceRequest request = AssociateUserWorkspaceRequest.builder()
                    .userId(user.getId())
                    .workspaceCode(workspaceCode)
                    .moduleProfileRightId(moduleProfileRight.getId())
                    .build();

            return save(userProfileFromRequest(request));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteAllUserProfileByUserId(Long userId) {
        getUserProfiles(userId.intValue())
                .stream()
                .filter(p -> Objects.equals(p.getUserId(), userId))
                .forEach(this::delete);
    }
}
