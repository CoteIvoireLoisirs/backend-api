package ca.deltagis.success.v1.application.services.project;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ca.deltagis.success.v1.adapters.web.http.project.request.ProjectSaveResponse;
import ca.deltagis.success.v1.adapters.web.http.project.request.ProjectsByWorkspaceResponse;
import ca.deltagis.success.v1.adapters.web.message.ProjectMessage;
import ca.deltagis.success.v1.adapters.web.message.files.FileUploadError;
import ca.deltagis.success.v1.application.services.files.FileNameBuilder;
import ca.deltagis.success.v1.application.services.files.FileStorageServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserAuthServiceImpl;
import ca.deltagis.success.v1.application.services.user.UserServiceImpl;
import ca.deltagis.success.v1.application.services.workspace.WorkspaceServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.project.Project;
import ca.deltagis.success.v1.domain.core.entities.project.UserProjectFavorite;
import ca.deltagis.success.v1.domain.core.entities.project.specificTaxes.ProjectSpecificTaxes;
import ca.deltagis.success.v1.domain.core.entities.workspace.WorkSpaceStatus;
import ca.deltagis.success.v1.domain.core.entities.workspace.Workspace;
import ca.deltagis.success.v1.domain.core.models.FileNameParam;
import ca.deltagis.success.v1.domain.core.models.FileOperationResponse;
import ca.deltagis.success.v1.domain.core.models.FileUploadResponse;
import ca.deltagis.success.v1.domain.core.models.project.ProjectStatus;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
import ca.deltagis.success.v1.domain.ports.in.project.IProjectService;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonService;
import ca.deltagis.success.v1.infrastructure.repository.project.ProjectRepository;
import ca.deltagis.success.v1.infrastructure.repository.project.ProjectSpecificTaxesRepository;
import ca.deltagis.success.v1.infrastructure.repository.workspace.WorkspaceRepository;
import ca.deltagis.success.v1.infrastructure.utils.DateUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter

@Service
public class ProjectServiceImpl extends AbstractCommonService<Project> implements IProjectService {

    @Autowired
    private ProjectRepository repository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserAuthServiceImpl userAuthService;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private WorkspaceServiceImpl workspaceService;

    @Autowired
    private UserProjectFavoriteImpl userProjectFavoriteService;

    @Autowired
    private FileStorageServiceImpl fileStorage;

    @Autowired
    FileStorageServiceImpl storageService;

    private String uploadFileDirectory = "project";

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Autowired
    private ProjectSpecificTaxesRepository projectSpecificTaxesRepository;

    @Override
    public List<Project> findAll() {
        return repository.findAll();
    }

    @Override
    public Long deleteById(Long id) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("project not found"));
        project.setStatus(ProjectStatus.DELETED);
        repository.deleteById(id);
        return id;
    }

    @Override
    public boolean existsById(Long id) {
        return repository.findById(id).isPresent();
    }

    @Override
    public Project save(Project entity) {
        return repository.save(entity);
    }

    @Override
    public ICommonRepository<Project> getRepository() {
        return repository;
    }

    @Override
    public Project getById(Long id) {
        Project findProject = repository.findById(id).orElse(null);
        if (findProject != null) {
            findProject.setOwner(userService.getOptionalUserById(findProject.getId()).orElse(null));
            findProject.setImageUrl(storageService.getUrlFile(findProject.getImagePath()));
        }
        return findProject;
    }

    @Override
    public List<Project> getAll() {
        try {
            List<Project> projects = getRepository()
                    .findAll(Sort.by(Sort.Direction.DESC, "id"))
                    .stream()
                    .peek(p -> p.setOwner(userService.getOptionalUserById(p.getId()).orElse(null)))
                    .peek(project -> project.setImageUrl(storageService.getUrlFile(project.getImagePath())))
                    .toList();

            List<UserProjectFavorite> favorites = userProjectFavoriteService
                    .getAllByUserId(userAuthService.getCurrentLoggedUser().getId());
            return sortByUserFavorite(projects, favorites);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves all projects from a specified workspace.
     *
     * @param workspaceCode The code of the workspace.
     * @return A list of projects that belong to the specified workspace.
     */
    @Override
    public ProjectsByWorkspaceResponse getProjectsFromWorkspace(String workspaceCode) {
        try {
            Workspace checkWorkspace = workspaceRepository.findByCode(workspaceCode).orElse(null);
            // || checkWorkspace.getDeleted() != null || checkWorkspace.getStatus() ==
            // WorkSpaceStatus.DELETED && checkWorkspace.getStatus() ==
            // WorkSpaceStatus.DISABLE)
            if (checkWorkspace == null) {
                return ProjectsByWorkspaceResponse.builder().projects(new ArrayList<>())
                        .error(ProjectMessage.WORKSPACE_NOT_FOUND)
                        .build();
            }

            return ProjectsByWorkspaceResponse.builder().projects(getAllAndSortByLoggedUserFavorite(workspaceCode))
                    .error(null)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ProjectsByWorkspaceResponse.builder().error(ProjectMessage.SOMETHING_WENT_WRONG).projects(null)
                    .build();
        }
    }

    /**
     * Retrieves all projects from a specified workspace by id.
     *
     * @param workspaceId The id of the workspace.
     * @return A list of projects that belong to the specified workspace.
     */
    @Override
    public List<Project> getProjectsFromWorkspaceId(Long workspaceId) {
        try {
            Workspace workspace = workspaceService.getByIdOptional(workspaceId).orElse(null);
            if (workspace != null) {
                return getAllAndSortByLoggedUserFavorite(workspace.getCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * Validates if a project can be saved.
     * <p>
     * A project can be saved only if it doesn't exist.
     *
     * @param project The project to be validated.
     * @return A response containing the project and a status message.
     */
    @Override
    public ProjectSaveResponse validationCreateProject(Project project) {
        try {
            Workspace workspace = workspaceRepository.findByCode(project.getWorkspaceCode()).orElse(null);
            if (workspace == null) {
                return new ProjectSaveResponse(project, ProjectMessage.WORKSPACE_NOT_FOUND);
            }

            if (workspace.getStatus() == WorkSpaceStatus.DELETED) {
                return new ProjectSaveResponse(project, ProjectMessage.WORKSPACE_DELETED);
            }

            if (codeAlreadyUsed(project)) {
                return new ProjectSaveResponse(project, ProjectMessage.PROJECT_EXISTS);
            }

            return new ProjectSaveResponse(project, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ProjectSaveResponse(project, ProjectMessage.SOMETHING_WENT_WRONG);
        }
    }

    /**
     * Validates if a project can be updated.
     * <p>
     * A project can be updated only if it exists and is not deleted.
     *
     * @param project The project to be validated.
     * @return A response containing the project and a status message.
     */
    @Override
    public ProjectSaveResponse validationUpdateProject(Project project) {
        if (project == null || project.getId() == null) {
            return new ProjectSaveResponse(project, ProjectMessage.PROJECT_NOT_FOUND);
        }

        try {
            if (isDeleted(project)) {
                return new ProjectSaveResponse(project, ProjectMessage.CANT_UPDATE_DELETED_PROJECT);
            }

            return new ProjectSaveResponse(project, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ProjectSaveResponse(project, ProjectMessage.SOMETHING_WENT_WRONG);
        }
    }

    /**
     * Validates if a project can be closed.
     * <p>
     * A project can be closed only if it has been previously archived.
     *
     * @param id The project to be validated.
     * @return A response containing the project and a status message.
     */
    @Override
    public ProjectSaveResponse validationCloseProject(int id) {
        if (id <= 0) {
            return new ProjectSaveResponse(null, ProjectMessage.PROJECT_NOT_FOUND);
        }

        try {
            Project project = repository.findById((long) id).orElse(null);

            if (project == null) {
                return new ProjectSaveResponse(null, ProjectMessage.PROJECT_NOT_FOUND);
            }

            ProjectSaveResponse validation = validationUpdateProject(project);

            if (validation.getError() != null) {
                return validation;
            }

            if (isClosed(project)) {
                return new ProjectSaveResponse(project, ProjectMessage.ALREADY_CLOSED);
            }

            return new ProjectSaveResponse(project, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ProjectSaveResponse(null, ProjectMessage.SOMETHING_WENT_WRONG);
        }
    }

    /**
     * Updates the image of a project.
     *
     * @param file The image file to be updated.
     * @param Code The code of the project to be updated.
     * @return The updated project.
     */

    public ProjectSaveResponse updateProjectImage(MultipartFile file, String code) {
        if (file == null || file.isEmpty()) {
            return new ProjectSaveResponse(null, ProjectMessage.CANT_UPLOAD_EMPTY_FILE);
        }

        Project project = repository.findByCode(code).orElseThrow(() -> new RuntimeException("project not found"));

        if (project == null) {
            return new ProjectSaveResponse(null, ProjectMessage.NOT_FOUND);
        }

        FileUploadResponse uploadResponse = changeProjectImage(file, project.getCode());

        if (uploadResponse.getError() != null) {
            return new ProjectSaveResponse(null, ProjectMessage.fromFileUploadError(uploadResponse.getError()));
        }

        project.setImagePath(uploadResponse.getFileName());
        repository.save(project);

        return new ProjectSaveResponse(project, ProjectMessage.NONE);

    }

    /**
     * Validates if a project can be archived.
     * <p>
     * A project can be archived only if it has been previously created.
     *
     * @param id The project to be validated.
     * @return A response containing the project and a status message.
     */
    @Override
    public ProjectSaveResponse validationArchiveProject(int id) {
        if (id <= 0) {
            return new ProjectSaveResponse(null, ProjectMessage.PROJECT_NOT_FOUND);
        }

        try {
            Project project = repository.findById((long) id).orElse(null);

            ProjectSaveResponse validation = validationCloseProject(id);

            if (validation.getError() != null) {
                return validation;
            }

            if (isArchived(project)) {
                return new ProjectSaveResponse(project, ProjectMessage.AlREADY_ARCHIVED);
            }

            return new ProjectSaveResponse(project, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ProjectSaveResponse(null, ProjectMessage.SOMETHING_WENT_WRONG);
        }
    }

    /**
     * Checks if a project with the given code exists.
     *
     * @param code The code of the project to check.
     * @return true if a project with the given code exists, false otherwise.
     */
    @Override
    public boolean existsByCode(String code) {
        return repository.findByCode(code).isPresent();
    }

    /**
     * Checks if a project code is already used.
     *
     * @param project The project to check.
     * @return true if the project code is already used, false otherwise.
     */
    @Override
    public boolean codeAlreadyUsed(Project project) {
        return existsByCode(project.getCode()) && project.getId() == null;
    }

    /**
     * Saves a project to the repository.
     *
     * @param project The project entity to be saved.
     * @return A response containing the saved project and a status message.
     */
    @Override
    public ProjectSaveResponse saveProject(Project project) {
        try {
            List<ProjectSpecificTaxes> taxes = project.getTaxes();
            project.setWorkspaceCode(project.getWorkspaceCode());

            ProjectSaveResponse validator = validationCreateProject(project);
            if (validator.getError() != null) {
                return validator;
            }

            return repository
                    .findByCode(project.getCode())
                    .map(this::validationCreateProject)
                    .orElseGet(() -> {
                        project.setAutoFields();
                        project.setStatus(ProjectStatus.PENDING);
                        project.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());

                        repository.save(project);
                        if (taxes != null && !taxes.isEmpty()) {
                            for (ProjectSpecificTaxes tax : taxes) {
                                tax.setProject(project);
                                tax.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());
                                saveProjectSpecificTaxes(tax);
                            }
                        }
                        return new ProjectSaveResponse(project, null);
                    });
        } catch (Exception e) {
            e.printStackTrace();
            return new ProjectSaveResponse(project, ProjectMessage.SOMETHING_WENT_WRONG);
        }
    }

    // public ProjectSaveResponse updateImageProject(MultipartFile file, String
    // code){

    // if (file ==null) {
    // return new ProjectSaveResponse(null, ProjectMessage.CANT_UPLOAD_EMPTY_FILE,
    // null);

    // }
    // FileUploadResponse uploadResponse = changeProjectImage(file, Project.get);

    // }

    /**
     * Updates a project to the repository.
     *
     * @param project The project entity to be updated.
     * @param id      The id of the project to be updated.
     * @return A response containing the updated project and a status message.
     */
    @Transactional
    @Override
    public ProjectSaveResponse updateProject(Project project, Long id) {
        try {
            Project existingProject = repository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Project not found"));

            // Validation
            ProjectSaveResponse validator = validationUpdateProject(project);
            if (validator.getError() != null) {
                return validator;
            }
            updateFields(project, existingProject);
            existingProject.getProjectSpecificTaxes().clear();
            List<ProjectSpecificTaxes> taxes = project.getTaxes();
            if (taxes != null && !taxes.isEmpty()) {
                existingProject.getProjectSpecificTaxes().clear();
                for (ProjectSpecificTaxes tax : taxes) {
                    tax.setProject(existingProject); // Synchroniser la relation bidirectionnelle
                    tax.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());
                    existingProject.getProjectSpecificTaxes().add(tax);
                }
            }
            Project updatedProject = repository.save(existingProject);
            return new ProjectSaveResponse(updatedProject, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ProjectSaveResponse(project, ProjectMessage.SOMETHING_WENT_WRONG);
        }
    }

    void updateFields(Project project, Project existingProject) {

        if (project.getImagePath() != null && !project.getImagePath().isEmpty()) {
            existingProject.setImagePath(project.getImagePath());
        } else if (project.getImagePath() != null && project.getImagePath().isEmpty()) {
            if (deleteProjectImage(existingProject)) {
                existingProject.setImagePath(null);
            }
        }
        existingProject.setCode(project.getCode());
        existingProject.setLibelle(project.getLibelle());
        existingProject.setType(project.getType());
        existingProject.setProjectNumber((String) project.getProjectNumber());
        existingProject.setCreated(project.getCreated());
        existingProject.setStartDate(project.getStartDate());
        existingProject.setEndDate(project.getEndDate());
        // existingProject.setDuration(project.getDuration());
        existingProject.setLocalCurrency(project.getLocalCurrency());
        existingProject.setOtherCurrencies(project.getOtherCurrencies());
        existingProject.setAccountingSystem(project.getAccountingSystem());
        existingProject.setContact(project.getContact());
        existingProject.setEmail(project.getEmail());
        existingProject.setCountry(project.getCountry());
        existingProject.setCity(project.getCity());
        existingProject.setAdress(project.getAdress());
        existingProject.setFormatAmount(project.getFormatAmount());
        existingProject.setFormatQuantity(project.getFormatQuantity());
        existingProject.setFormatRate(project.getFormatRate());

        existingProject.setCostabUse(project.isCostabUse());
        existingProject.setBudgetTrackingUse(project.isBudgetTrackingUse());
        existingProject.setSigfipUse(project.isSigfipUse());
        existingProject.setPaymentOrderUse(project.isPaymentOrderUse());
        existingProject.setWasfUse(project.isWasfUse());
        existingProject.setTaxAccountingUse(project.isTaxAccountingUse());

        if (project.getImagePath() != null && project.getImagePath() != "") {
            existingProject.setImagePath(project.getImagePath());
        } else if (project.getImagePath() == "") {
            // TO DO delete file

            existingProject.setImagePath(null);
        }

        // existingProject.setOwnerId(project.getOwnerId());
        existingProject.setUpdateBy(userAuthService.getCurrentLoggedUser().getId());
        existingProject.setWorkspaceCode(project.getWorkspaceCode());
    }

    public Boolean deleteProjectImage(Project project) {
        String path = project.getImagePath();
        if (path != null && !path.isEmpty()) {
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
     * Archives a project.
     * <p>
     * A project can be archived only if it has been previously created.
     *
     * @param id The project to be archived.
     * @return A response containing the archived project and a status message.
     */
    @Override
    public ProjectSaveResponse archiveProject(int id) {
        try {
            ProjectSaveResponse validationCloseProject = validationArchiveProject(id);

            if (validationCloseProject.getError() != null || validationCloseProject.getProject() == null) {
                return validationCloseProject;
            }

            Project project = validationCloseProject.getProject();
            project.setStatus(ProjectStatus.ARCHIVED);
            repository.save(project);
            return new ProjectSaveResponse(project, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ProjectSaveResponse(null, ProjectMessage.SOMETHING_WENT_WRONG);
        }
    }

    /**
     * Closes a project.
     * <p>
     * A project can be closed only if it has been previously archived.
     *
     * @param id The project to be closed.
     * @return A response containing the closed project and a status message.
     */
    @Override
    public ProjectSaveResponse closeProject(int id) {
        try {
            ProjectSaveResponse validationCloseProject = validationCloseProject(id);

            if (validationCloseProject.getError() != null || validationCloseProject.getProject() == null) {
                return validationCloseProject;
            }

            Project project = validationCloseProject.getProject();
            project.setStatus(ProjectStatus.CLOSED);
            repository.save(project);
            return new ProjectSaveResponse(project, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ProjectSaveResponse(null, ProjectMessage.SOMETHING_WENT_WRONG);
        }
    }

    /**
     * Closes a project.
     * <p>
     * A project can be closed only if it has been previously archived.
     *
     * @param project The project to be closed.
     * @return A response containing the closed project and a status message.
     */
    @Override
    public boolean isDeleted(Project project) {
        return project != null && project.getStatus() == ProjectStatus.DELETED;
    }

    /**
     * Checks if a project is closed.
     *
     * @param project The project to check.
     * @return true if the project is closed, false otherwise.
     */
    @Override
    public boolean isClosed(Project project) {
        return project != null && project.getStatus() == ProjectStatus.CLOSED;
    }

    /**
     * Checks if a project is archived.
     *
     * @param project The project to check.
     * @return true if the project is archived, false otherwise.
     */
    @Override
    public boolean isArchived(Project project) {
        return project != null && project.getStatus() == ProjectStatus.ARCHIVED;
    }

    /**
     * Save project specific taxes.
     *
     * @param projectSpecificTaxes projectSpecificTaxes to add.
     * @return A true if the specific tax is saved.
     */
    @Override
    public boolean saveProjectSpecificTaxes(ProjectSpecificTaxes projectSpecificTaxes) {
        try {
            projectSpecificTaxesRepository.save(projectSpecificTaxes);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all projects from a specified workspace and sorts them by user
     * favorites.
     *
     * @param workspaceCode The code of the workspace.
     * @return A list of projects that belong to the specified workspace and are
     *         sorted by user favorites.
     */
    @Override
    public List<Project> getAllAndSortByLoggedUserFavorite(String workspaceCode) {
        try {
            List<Project> projects = repository.findByWorkspaceCode(workspaceCode);
            List<UserProjectFavorite> favorites = userProjectFavoriteService
                    .getAllByUserId(userAuthService.getCurrentLoggedUser().getId());
            projects.forEach(project -> project.setImageUrl(storageService.getUrlFile(project.getImagePath())));

            return sortByUserFavorite(projects, favorites);
        } catch (Exception e) {
            logger.warn("Failed to retrieve projects from workspace: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Sorts a list of projects based on user favorites.
     *
     * @param projects  The list of projects to sort.
     * @param favorites The list of user favorites.
     * @return The sorted list of projects.
     */
    @Override
    public List<Project> sortByUserFavorite(List<Project> projects, List<UserProjectFavorite> favorites) {
        logger.info("Projects: {}, favorites: {}", projects, favorites);

        if (projects == null || projects.isEmpty()) {
            return new ArrayList<>();
        }

        if (favorites == null || favorites.isEmpty()) {
            return new ArrayList<>(projects); // Retourner une nouvelle liste modifiable
        }

        // IDs favorite
        Set<Long> favoriteProjectIds = favorites
                .stream()
                .map(UserProjectFavorite::getProjectId)
                .collect(Collectors.toSet());

        // sortable list
        List<Project> sortableProjects = new ArrayList<>(projects);

        // sort
        sortableProjects.sort((p1, p2) -> {
            boolean p1IsFavorite = favoriteProjectIds.contains(p1.getId());
            boolean p2IsFavorite = favoriteProjectIds.contains(p2.getId());

            if (p1IsFavorite && !p2IsFavorite) {
                return -1;
            } else if (!p1IsFavorite && p2IsFavorite) {
                return 1;
            } else {
                return Long.compare(p1.getId(), p2.getId());
            }
        });

        for (Project project : sortableProjects) {
            project.setFavorite(favoriteProjectIds.contains(project.getId()));
        }

        return sortableProjects;
    }

    public FileUploadResponse changeProjectImage(MultipartFile file, String code) {
        if (file == null) {
            return new FileUploadResponse(null, FileUploadError.EMPTY_FILE);
        }

        if (code == null) {
            return new FileUploadResponse(null, FileUploadError.INVALID_DESTINATION_PATH);
        }

        LocalDateTime dateTime = DateUtil.getCurrentDateTime();
        FileNameParam param = new FileNameBuilder()
                .file(file)
                .folder(getDirectory(code))
                .newName(code)
                .buiFileNameParam();

        return storageService.storeImage(file, param);
    }

    /**
     * Updates the status of a workspace to disable and returns the
     * updated workspace in a successful ApiResponse. In case of an
     * exception or if the workspace is already disabled, an error
     * response is returned.
     *
     * @param code the code of the workspace to be disabled.
     * @return ResponseEntity containing the updated workspace or an
     *         error message.
     */

    public String getDirectory(String code) {
        String targetProject = code == null ? "" : code;
        return uploadFileDirectory + "/" + targetProject;
    }

}
