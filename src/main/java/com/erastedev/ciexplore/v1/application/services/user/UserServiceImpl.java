package com.erastedev.ciexplore.v1.application.services.user;

import com.erastedev.ciexplore.v1.application.validator.in.CommonValidation;
import com.erastedev.ciexplore.v1.application.validator.in.CommonError;
import com.erastedev.ciexplore.v1.adapters.web.message.files.FileUploadError;
import com.erastedev.ciexplore.v1.application.services.files.FileNameBuilder;
import com.erastedev.ciexplore.v1.application.services.files.FileStorageServiceImpl;
import com.erastedev.ciexplore.v1.application.services.workspace.WorkspaceServiceImpl;
import com.erastedev.ciexplore.v1.application.validator.out.user.CreateUserValidator;
import com.erastedev.ciexplore.v1.domain.entities.user.User;
import com.erastedev.ciexplore.v1.domain.entities.workspace.Workspace;
import com.erastedev.ciexplore.v1.domain.models.FileNameParam;
import com.erastedev.ciexplore.v1.domain.models.FileUploadResponse;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserDeleteResponse;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserRegisterAttempt;
import com.erastedev.ciexplore.v1.domain.entities.user.model.UserRegisterState;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonRepository;
import com.erastedev.ciexplore.v1.domain.ports.in.user.IUserService;
import com.erastedev.ciexplore.v1.domain.ports.out.AbstractCommonService;
import com.erastedev.ciexplore.v1.infrastructure.repository.user.UserRepository;
import com.erastedev.ciexplore.v1.infrastructure.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl extends AbstractCommonService<User> implements IUserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private WorkspaceServiceImpl workspaceService;

    @Autowired
    UserAuthServiceImpl userAuthService;

    @Autowired
    private CreateUserValidator validator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    FileStorageServiceImpl storageService;

    private final String uploadFileDirectory = "users";

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Retrieves all users from the repository.
     *
     * @return a list of all users
     */
    @Override
    public List<User> findAll() {
        return repository.findAllByDeletedIsNull();
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
    public User save(User entity) {
        try {
            if (entity.getId() == null) {
                entity.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());
                entity.setAutoFields();
            }
            logger.info("save >> user updated: {}", entity);
            return repository.save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ICommonRepository<User> getRepository() {
        return repository;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to find
     * @return the user associated with the given username, or null if no such user exists
     */
    @Override
    public User getUserByUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param username the email address of the user to find
     * @return the user associated with the given email address, or null if no such user exists
     */
    @Override
    public User getUserByEmail(String username) {
        return repository.findByEmail(username).orElse(null);
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to find
     * @return an Optional containing the user associated with the given email address, or an empty Optional if no such user exists
     */
    @Override
    public Optional<User> getOptionalUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the user associated with the given username, or an empty Optional if no such user exists
     */
    @Override
    public Optional<User> getOptionalUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Optional<User> getOptionalUserByEmailOrUsername(String login) {
        Optional<User> checkByEmail = getOptionalUserByEmail(login);
        if (checkByEmail.isPresent()) {
            logger.info("User with email {} found", login);
            return checkByEmail;
        }

        logger.info("User with email {} not found", login);
        return getOptionalUserByUsername(login);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to find
     * @return an Optional containing the user associated with the given ID, or an empty Optional if no such user exists
     */
    @Override
    public Optional<User> getOptionalUserById(Long id) {
        return repository.findById(id);
    }

    private User saveUser(User user) {
        try {
            user.setDisabled(false);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setAutoFields();
            user.setUpdateBy(userAuthService.getLoggedUserId());
            return save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new user from a given workspace code.
     *
     * @param user          the user to create
     * @param workspaceCode the workspace code to associate to the user
     * @return a UserRegisterAttempt containing the result of the operation and the user created
     */
    @Override
    public UserRegisterAttempt createUserFromWorkspace(User user, String workspaceCode) {
        try {
            // check if modules, rights are not empty
            // profileService.initializeDefaultData();

            Workspace workspace = workspaceService.getByCode(workspaceCode).orElse(null);
            UserRegisterAttempt createAttempt = validator.validateCreateUserAndAssociateToWorkspace(user, workspace);
            if (createAttempt != null) {
                return createAttempt;
            }

            // create user
            User userSaved = saveUser(user);

            return UserRegisterAttempt.builder().user(userSaved).state(null).success(true).build();
        } catch (Exception e) {
            e.printStackTrace();
            return UserRegisterAttempt.builder().user(null).state(UserRegisterState.SOMETHING_WENT_WRONG).success(false).build();
        }
    }

    @Override
    public UserRegisterAttempt createUserWithoutAssociation(User user) {
        try {
            // check if modules, rights are not empty
            // profileService.initializeDefaultData();

            UserRegisterAttempt validation = validator.createUserValidation(user);
            if (validation != null) {
                return validation;
            }

            // create user
            User userSaved = saveUser(user);

            return UserRegisterAttempt.builder().user(userSaved).state(null).success(true).build();
        } catch (Exception e) {
            e.printStackTrace();
            return UserRegisterAttempt.builder().user(null).state(UserRegisterState.SOMETHING_WENT_WRONG).success(false).build();
        }
    }

    /**
     * Updates an existing user.
     *
     * @param user the user to update
     * @return a UserRegisterAttempt containing the result of the operation and the user updated
     */
    @Override
    public UserRegisterAttempt updateUser(User user) {
        try {
            UserRegisterAttempt validation = validator.updateUserValidation(user);

            if (validation != null) {
                return validation;
            }

            User oldUser = getById(user.getId());

            // Update password if not null else keep the old password
            if (!Objects.equals(user.getPassword(), oldUser.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            user.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());
            user.setUpdated(DateUtil.getCurrentTimestamp());

            return UserRegisterAttempt.builder().user(save(user)).state(null).success(true).build();
        } catch (Exception e) {
            e.printStackTrace();
            return UserRegisterAttempt.builder().user(null).state(UserRegisterState.SOMETHING_WENT_WRONG).success(false).build();
        }
    }

    /**
     * Enables a user.
     *
     * @param userId the ID of the user to enable
     * @return a UserRegisterAttempt containing the result of the enable operation
     */
    @Override
    public UserRegisterAttempt enableUser(Long userId) {
        try {
            User user = getOptionalUserById(userId).orElse(null);
            UserRegisterAttempt validation = validator.enableOrDisableUserValidation(user);

            if (validation != null) {
                return validation;
            }
            if (user != null) {
                User userUpdated = enableOrDisableUser(user, false);
                return UserRegisterAttempt.builder().user(userUpdated).state(null).success(true).build();
            }

            return UserRegisterAttempt.builder().user(null).state(UserRegisterState.USER_NOT_FOUND).success(false).build();
        } catch (Exception e) {
            e.printStackTrace();
            return UserRegisterAttempt.builder().user(null).state(UserRegisterState.SOMETHING_WENT_WRONG).success(false).build();
        }
    }

    /**
     * Disables a user.
     *
     * @param userId the ID of the user to disable
     * @return a UserRegisterAttempt containing the result of the disable operation
     */
    @Override
    public UserRegisterAttempt disableUser(Long userId) {
        try {
            User user = getOptionalUserById(userId).orElse(null);
            UserRegisterAttempt validation = validator.enableOrDisableUserValidation(user);

            if (validation != null) {
                return validation;
            }

            if (user != null) {
                User userUpdated = enableOrDisableUser(user, true);
                return UserRegisterAttempt.builder().user(userUpdated).state(null).success(true).build();
            }

            return UserRegisterAttempt.builder().user(null).state(UserRegisterState.USER_NOT_FOUND).success(false).build();
        } catch (Exception e) {
            e.printStackTrace();
            return UserRegisterAttempt.builder().user(null).state(UserRegisterState.SOMETHING_WENT_WRONG).success(false).build();
        }
    }

    @Override
    public User enableOrDisableUser(User user, boolean enabled) {
        user.setDisabled(enabled);
        user.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());
        user.setUpdated(DateUtil.getCurrentTimestamp());
        logger.info("enableOrDisableUser >> user updated: {}", user);
        return save(user);
    }

    /**
     * Updates the profile image of a user.
     *
     * @param file   the profile image to upload
     * @param userId the ID of the user to update
     * @return a UserRegisterAttempt containing the result of the update
     */
    @Override
    public UserRegisterAttempt updateUserImage(MultipartFile file, Long userId) {
        try {
            // validation
            if (file == null) {
                return UserRegisterAttempt.builder().state(UserRegisterState.CANT_UPLOAD_EMPTY_FILE).build();
            }

            User user = getByIdOptional(userId).orElse(null);
            if (user == null) {
                return UserRegisterAttempt.builder().state(UserRegisterState.USER_NOT_FOUND).build();
            }

            FileUploadResponse uploadResponse = changeUserImage(file, user);

            if (uploadResponse.getError() != null) {
                return UserRegisterAttempt.builder()
                        .user(null).success(false).state(UserRegisterState.fromFileUploadError(uploadResponse.getError()))
                        .build();
            }

            // update user
            // user.setAvatar(uploadResponse.getFileName());
            user.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());
            user.setUpdated(DateUtil.getCurrentTimestamp());
            repository.save(user);

            return UserRegisterAttempt.builder()
                    .user(user).success(true)
                    .state(UserRegisterState.fromFileUploadError(uploadResponse.getError()))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return UserRegisterAttempt.builder().user(null).success(false).state(UserRegisterState.SOMETHING_WENT_WRONG).build();
        }
    }

    /**
     * Updates the profile image of a user.
     *
     * @param file the profile image to upload
     * @param user the user to update
     * @return a FileUploadResponse containing the result of the update
     */
    public FileUploadResponse changeUserImage(MultipartFile file, User user) {
        try {
            if (file == null) {
                return new FileUploadResponse(null, FileUploadError.EMPTY_FILE);
            } else if (user == null || user.getUuid() == null) {
                return new FileUploadResponse(null, FileUploadError.INVALID_DESTINATION_PATH);
            }

            FileNameParam param = new FileNameBuilder()
                    .file(file)
                    .folder(getDirectory(user.getUuid().toString()))
                    .newName(user.getUuid().toString())
                    .buiFileNameParam();

            return storageService.storeImage(file, param);
        } catch (Exception e) {
            e.printStackTrace();
            return new FileUploadResponse(null, FileUploadError.SOMETHING_WENT_WRONG);
        }
    }

    public String getDirectory(String userUuid) {
        String targetWorkspace = userUuid == null ? "" : userUuid;
        return uploadFileDirectory + "/" + targetWorkspace;
    }

    @Override
    public UserDeleteResponse deleteUser(Long id) {
        try {
            User user = getByIdOptional(id).orElse(null);
            if (user == null) {
                return UserDeleteResponse.builder().success(false).state(UserRegisterState.USER_NOT_FOUND).build();
            }

            // delte user
            delete(user);
            return UserDeleteResponse.builder().success(true).state(null).build();
        } catch (Exception e) {
            e.printStackTrace();
            return UserDeleteResponse.builder().success(false).state(UserRegisterState.SOMETHING_WENT_WRONG).build();
        }
    }

    /**
     * Retrieves all users from the database.
     * <p>
     * This method checks if the logged-in user is an admin. If not, a validation error is returned.
     * Otherwise, a list of all users is retrieved from the database and returned in a successful
     * validation response.
     * <p>
     *
     * @return a validation response containing the list of users or an error
     */
    @Override
    public CommonValidation<List<User>, CommonError> getAllUsers(String workspaceCode) {
        // TODO : Ensuring the logged-in user is admin
        try {
            List<User> users = withUserProfile(findAll(), workspaceCode);
            return new CommonValidation<>(users, null, true);
        } catch (Exception e) {
            logger.error("Failed to retrieve users: {}", e.getMessage());
            return new CommonValidation<>(null, CommonError.SOMETHING_WENT_WRONG, false);
        }
    }

    @Override
    public List<User> withUserProfile(List<User> userList, String workspaceCode) {
        List<User> users = new ArrayList<>();

        try {
            for (User user : userList) {
                if (user != null && user.getDeleted() == null) {
                    users.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sortUsersByWorkspaceAccess(users, workspaceCode);
    }

    public List<User> sortUsersByWorkspaceAccess(List<User> users, String workspaceCode) {
        return users;
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     */
    @Override
    public User getUser(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Encodes the provided password using a password encoder.
     *
     * @param password the password to encode
     * @return a String instance with the encoded password
     */
    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
