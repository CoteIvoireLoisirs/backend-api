/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-27 13:04:59
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-27 13:07:14
 */
package com.erastedev.ciexplore.v1.application.services.workspace;

import com.erastedev.ciexplore.v1.application.request.workspace.WorkspaceSaveResponse;
import com.erastedev.ciexplore.v1.adapters.web.message.WorkspaceMessage;
import com.erastedev.ciexplore.v1.adapters.web.message.files.FileUploadError;
import com.erastedev.ciexplore.v1.application.services.files.FileNameBuilder;
import com.erastedev.ciexplore.v1.application.services.files.FileStorageServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.UserAuthServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.UserProfileServiceImpl;
import com.erastedev.ciexplore.v1.application.services.user.UserServiceImpl;
import com.erastedev.ciexplore.v1.domain.entities.workspace.WorkSpaceStatus;
import com.erastedev.ciexplore.v1.domain.entities.workspace.Workspace;
import com.erastedev.ciexplore.v1.domain.models.FileNameParam;
import com.erastedev.ciexplore.v1.domain.models.FileOperationResponse;
import com.erastedev.ciexplore.v1.domain.models.FileUploadResponse;
import com.erastedev.ciexplore.v1.domain.models.workspace.WorkspacePublic;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonRepository;
import com.erastedev.ciexplore.v1.domain.ports.in.workspace.IWorkspaceService;
import com.erastedev.ciexplore.v1.domain.ports.out.AbstractCommonService;
import com.erastedev.ciexplore.v1.infrastructure.repository.workspace.WorkspaceRepository;
import com.erastedev.ciexplore.v1.infrastructure.utils.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Getter
@Setter

@Service
public class WorkspaceServiceImpl extends AbstractCommonService<Workspace> implements IWorkspaceService {
    @Autowired
    private WorkspaceRepository repository;

    @Autowired
    private FileStorageServiceImpl fileStorage;

    @Autowired
    private FileStorageServiceImpl serviceFile;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserAuthServiceImpl userAuthService;

    @Autowired
    private UserProfileServiceImpl userProfileService;

    @Autowired
    FileStorageServiceImpl storageService;

    private String uploadFileDirectory = "workspaces";

    private Logger logger = LoggerFactory.getLogger(WorkspaceServiceImpl.class);

    @Override
    public List<Workspace> findAll() {
        return repository.findAll();
    }

    @Override
    public Workspace getById(Long id) {
        Workspace findWorkspace = repository.findById(id).orElse(null);
        if (findWorkspace != null) {
            findWorkspace.setOwner(userService.getOptionalUserById(findWorkspace.getOwnerId()).orElse(null));
            findWorkspace.setImageUrl(storageService.getUrlFile(findWorkspace.getImagePath()));
        }
        return findWorkspace;
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
    public Workspace save(Workspace entity) {
        return repository.save(entity);
    }

    @Override
    public ICommonRepository<Workspace> getRepository() {
        return repository;
    }

    @Override
    public List<Workspace> getAll() {
        return getRepository()
                .findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .peek(workspace -> workspace.setOwner(userService.getOptionalUserById(workspace.getOwnerId()).orElse(null)))
                .peek(workspace -> workspace.setImageUrl(storageService.getUrlFile(workspace.getImagePath())))
                .peek(workspace -> workspace.setProjectCount(countProjectByWorkspaceCode(workspace.getCode())))
                .peek(workspace -> workspace.setUserCount(countUserByWorkspaceCode(workspace.getCode())))
                .toList();
    }

    /**
     * Retrieves a workspace by its code.
     *
     * @param code the code of the workspace to be retrieved.
     * @return an Optional containing the workspace if found, or empty if not found.
     */
    @Override
    public Optional<Workspace> getByCode(String code) {
        return repository.findByCode(code);
    }

    /**
     * Retrieves a workspace by its id.
     *
     * @param id the code of the workspace to be retrieved.
     * @return an Optional containing the workspace if found, or empty if not found.
     */
    public Workspace getByIdWorkspace(Long id) {
        Workspace workspace = repository.findById(id).orElse(null);
        if (workspace == null) {
            return null;
        }
        workspace.setProjectCount(countProjectByWorkspaceCode(workspace.getCode()));
        workspace.setUserCount(countUserByWorkspaceCode(workspace.getCode()));
        return workspace;
    }

    /**
     * Retrieves a workspace by its code.
     *
     * @return an Optional containing the workspace if found, or empty if not found.
     */
    @Override
    public List<WorkspacePublic> getWorkspaceWithOutAuthorization() {
        List<Workspace> workspaces = getAll();
        if (workspaces.isEmpty()) {
            return null;
        }

        return workspaces.stream().map(WorkspacePublic::buildFromWorkspace).toList();
    }

    /**
     * Saves a workspace.
     *
     * @param workspace The workspace to be saved.
     * @return a response containing the workspace if saved successfully, or an
     * error message if not.
     */
    @Override
    public WorkspaceSaveResponse saveWorkspace(Workspace workspace) {
        try {
            return repository
                    .findByCode(workspace.getCode())
                    .map(existingWorkspace -> {
                        if (workspace.getStatus() == WorkSpaceStatus.PENDING) {
                            return new WorkspaceSaveResponse(existingWorkspace, WorkspaceMessage.ALREADY_PENDING, null);
                        } else {
                            return new WorkspaceSaveResponse(workspace, WorkspaceMessage.ALREADY_EXISTS, null);
                        }
                    }).orElseGet(() -> {
                        // Otherwise, save the workspace
                        workspace.setAutoFields();
                        workspace.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());

                        repository.save(workspace);
                        return new WorkspaceSaveResponse(workspace, WorkspaceMessage.NONE, null);
                    });
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, String> errorDetail = new HashMap<>();
            errorDetail.put("message", e.getMessage());
            return new WorkspaceSaveResponse(null, WorkspaceMessage.SOMETHING_WENT_WRONG, errorDetail);
        }
    }

    /**
     * Verifies if a user can access to a workspace by its code.
     *
     * @param userId        the ID of the user to be verified.
     * @param workspaceCode the code of the workspace to be verified.
     * @return true if the user can access, false otherwise.
     */
    @Override
    public boolean userCanAccessToWorkspace(String workspaceCode, Long userId) {
        return userProfileService.existsByWorkspaceCodeAndUserId(workspaceCode, userId);
    }

    /**
     * Enable a workspace.
     *
     * @param code The workspace to be saved.
     * @return a response containing the workspace if saved successfully, or an
     * error message if not.
     * <p>
     * this code allows you to set the status of a user's workspace to
     * activated
     */

    public WorkspaceSaveResponse activeWorkspace(String code) {
        Workspace workspace = repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        LocalDateTime limitTime;
        if (workspace.getLimitTime() != null) {
            limitTime = workspace.getLimitTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

        }

        LocalDateTime nextDate = DateUtil.getNexTimeDateTime();
        Date limitTimeAsDate = Date.from(nextDate.atZone(ZoneId.systemDefault()).toInstant());

        if (workspace.getStatus() == WorkSpaceStatus.PENDING) {
            return new WorkspaceSaveResponse(workspace, WorkspaceMessage.ALREADY_PENDING, null);
        }
        workspace.setStatus(WorkSpaceStatus.PENDING);
        workspace.setLimitTime(limitTimeAsDate);
        repository.save(workspace);
        return new WorkspaceSaveResponse(workspace, WorkspaceMessage.ACTIVATED_SUCCESSFULLY, null);
    }

    /**
     * Disable a workspace.
     *
     * @param code The workspace to be saved.
     * @return a response containing the workspace if saved successfully, or an
     * error message if not.
     * <p>
     * this code allows you to set the status of a user's workspace to
     * disable
     */

    public WorkspaceSaveResponse disableWorkspace(String code) {
        // Récupère le workspace ou lève une exception s'il n'existe pas
        Workspace workspace = repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        // Vérifie si la date limite existe pour éviter un NullPointerException
        LocalDateTime limitTime = null;
        if (workspace.getLimitTime() != null) {
            limitTime = workspace.getLimitTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

        }
        LocalDateTime previousDate = DateUtil.getPreviousDate();
        Date limitTimeAsDate = Date.from(previousDate.atZone(ZoneId.systemDefault()).toInstant());

        if (workspace.getStatus() == WorkSpaceStatus.DISABLE) {

            return new WorkspaceSaveResponse(workspace, WorkspaceMessage.ALREADY_DISABLE, null);
        }

        // Vérifie le statut et désactive si nécessaire
        if ((limitTime != null && limitTime.isBefore(DateUtil.getCurrentDateTime()))
                || workspace.getStatus() == WorkSpaceStatus.PENDING) {
            workspace.setStatus(WorkSpaceStatus.DISABLE);
            workspace.setLimitTime(limitTimeAsDate);

            repository.save(workspace);
            return new WorkspaceSaveResponse(workspace, WorkspaceMessage.DISABLE, null);
        }

        // Si aucune condition n'est remplie, retourne une erreur de statut
        return new WorkspaceSaveResponse(workspace, WorkspaceMessage.ERROR_STATUS, null);
    }

    /**
     * Updates the status of a workspace to disable and returns the
     * updated workspace in a successful ApiResponse. In case of an
     * exception or if the workspace is already disabled, an error
     * response is returned.
     *
     * @param workspaceCode the code of the workspace to be disabled.
     * @return ResponseEntity containing the updated workspace or an
     * error message.
     */
    @Override
    public String getDirectory(String workspaceCode) {
        String targetWorkspace = workspaceCode == null ? "" : workspaceCode;
        return uploadFileDirectory + "/" + targetWorkspace;
    }

    /**
     * Update a workspace find by its id and return the updated workspace in a
     * success
     * or an exception
     *
     * @param workspace
     * @return ResponseEntity containing the updated workspace or an
     * error message.
     */
    public WorkspaceSaveResponse updateWorkspace(Workspace workspace) {
        try {
            Workspace lastModifWorkspace = repository.findById(workspace.getId())
                    .orElseThrow(() -> new RuntimeException("Workspace not found"));
            fillWorkspaceFields(lastModifWorkspace, workspace);
            Workspace updatedWorkspace = repository.save(lastModifWorkspace);
            return new WorkspaceSaveResponse(updatedWorkspace, WorkspaceMessage.UPDATED, null);
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, String> errorDetail = new HashMap<>();
            errorDetail.put("message", e.getMessage());
            return new WorkspaceSaveResponse(null, WorkspaceMessage.SOMETHING_WENT_WRONG, errorDetail);
        }
    }

    void fillWorkspaceFields(Workspace oldWorkspace, Workspace newWorkspace) {
        if (newWorkspace.getImagePath() != null && !newWorkspace.getImagePath().isEmpty()) {
            oldWorkspace.setImagePath(newWorkspace.getImagePath());
        } else if (newWorkspace.getImagePath() != null && newWorkspace.getImagePath().isEmpty()) {
            if (deleteWorkspaceImage(oldWorkspace)) {
                oldWorkspace.setImagePath(null);
            }
        }
        oldWorkspace.setName(newWorkspace.getName());
        oldWorkspace.setDescription(newWorkspace.getDescription());
        oldWorkspace.setLimitProject(newWorkspace.getLimitProject());
        oldWorkspace.setLimitUser(newWorkspace.getLimitUser());
    }

    Boolean deleteWorkspaceImage(Workspace workspace) {
        String path = workspace.getImagePath();
        if (path != "" || path != null) {
            FileOperationResponse response = fileStorage.deleteFile(path);
            if (!response.isSuccess()) {
                logger.warn("Failed to delete file: {}", response.getMessage());
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Updates the image of a workspace.
     *
     * @param file          The image file to be updated.
     * @param workspaceCode The code of the workspace to be updated.
     * @return The updated workspace.
     */
    @Override
    public WorkspaceSaveResponse updateWorkspaceImage(MultipartFile file, String workspaceCode) {
        if (file == null) {
            return new WorkspaceSaveResponse(null, WorkspaceMessage.CANT_UPLOAD_EMPTY_FILE, null);
        }

        Workspace workspace = repository.findByCode(workspaceCode).orElse(null);
        if (workspace == null) {
            return new WorkspaceSaveResponse(null, WorkspaceMessage.NOT_FOUND, null);
        }

        FileUploadResponse uploadResponse = changeWorkspaceImage(file, workspace.getCode());

        if (uploadResponse.getError() != null) {
            return new WorkspaceSaveResponse(
                    null,
                    WorkspaceMessage.fromFileUploadError(uploadResponse.getError()),
                    null);
        }

        workspace.setImagePath(uploadResponse.getFileName());
        repository.save(workspace);

        return new WorkspaceSaveResponse(workspace, WorkspaceMessage.NONE, null);
    }

    /**
     * Gets the directory where the workspace files are stored.
     *
     * @param file          the file to upload.
     * @param workspaceCode the code of the workspace.
     * @return the directory where the workspace files are stored.
     */
    @Override
    public FileUploadResponse changeWorkspaceImage(MultipartFile file, String workspaceCode) {
        if (file == null) {
            return new FileUploadResponse(null, FileUploadError.EMPTY_FILE);
        }

        if (workspaceCode == null) {
            return new FileUploadResponse(null, FileUploadError.INVALID_DESTINATION_PATH);
        }

        LocalDateTime dateTime = DateUtil.getCurrentDateTime();
        FileNameParam param = new FileNameBuilder()
                .file(file)
                .folder(getDirectory(workspaceCode))
                .newName(workspaceCode)
                .buiFileNameParam();

        return storageService.storeImage(file, param);
    }

    /**
     * Counts the number of projects associated with a given workspace code.
     *
     * @param workspaceCode the code of the workspace.
     * @return the number of projects in the specified workspace.
     */
    @Override
    public int countProjectByWorkspaceCode(String workspaceCode) {
        // TODO : fix
        return 0;
    }

    /**
     * Counts the number of users associated with a given workspace code.
     *
     * @param workspaceCode the code of the workspace.
     * @return the number of users associated with the workspace.
     */
    @Override
    public int countUserByWorkspaceCode(String workspaceCode) {
        return userService.getUsersFromWorkspace(workspaceCode).size();
    }
}
