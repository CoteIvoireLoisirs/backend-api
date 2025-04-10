package ca.deltagis.success.v1.domain.ports.in.project;


import ca.deltagis.success.v1.adapters.web.http.project.request.ProjectSaveResponse;
import ca.deltagis.success.v1.adapters.web.http.project.request.ProjectsByWorkspaceResponse;
import ca.deltagis.success.v1.domain.core.entities.project.Project;
import ca.deltagis.success.v1.domain.core.entities.project.specificTaxes.ProjectSpecificTaxes;
import ca.deltagis.success.v1.domain.core.entities.project.UserProjectFavorite;

import java.util.List;

public interface IProjectService {

    /**
     * Retrieves all projects from a specified workspace.
     *
     * @param workspaceCode The code of the workspace.
     * @return A list of projects that belong to the specified workspace.
     */
    ProjectsByWorkspaceResponse getProjectsFromWorkspace(String workspaceCode);

    /**
     * Retrieves all projects from a specified workspace by id.
     *
     * @param workspaceId The id of the workspace.
     * @return A list of projects that belong to the specified workspace.
     */
    List<Project> getProjectsFromWorkspaceId(Long workspaceId); // TODO : remove to use getProjectsFromWorkspace

    /**
     * Validates if a project can be saved.
     * <p>
     * A project can be saved only if it doesn't exist.
     *
     * @param project The project to be validated.
     * @return A response containing the project and a status message.
     */
    ProjectSaveResponse validationCreateProject(Project project);

    /**
     * Validates if a project can be updated.
     * <p>
     * A project can be updated only if it exists.
     *
     * @param project The project to be validated.
     * @return A response containing the project and a status message.
     */
    ProjectSaveResponse validationUpdateProject(Project project);

    /**
     * Validates if a project can be closed.
     * <p>
     * A project can be closed only if it has been previously archived.
     *
     * @param id The project to be validated.
     * @return A response containing the project and a status message.
     */
    ProjectSaveResponse validationCloseProject(int id);

    /**
     * Validates if a project can be archived.
     * <p>
     * A project can be archived only if it has been previously created.
     *
     * @param id The project to be validated.
     * @return A response containing the project and a status message.
     */
    ProjectSaveResponse validationArchiveProject(int id);

    /**
     * Saves a project to the repository.
     *
     * @param project The project entity to be saved.
     * @return A response containing the saved project and a status message.
     */
    ProjectSaveResponse saveProject(Project project);


    /**
     * Updates a project to the repository.
     *
     * @param project The project entity to be updated.
     * @param id      The ID of the project to be updated.
     * @return A response containing the updated project and a status message.
     */
    ProjectSaveResponse updateProject(Project project, Long id);

    /**
     * Checks if a project with the given code exists.
     *
     * @param code The code of the project to check.
     * @return true if a project with the given code exists, false otherwise.
     */
    boolean existsByCode(String code);

    /**
     * Checks if a project code is already used.
     *
     * @param project The project to check.
     * @return true if the project code is already used, false otherwise.
     */
    boolean codeAlreadyUsed(Project project);

    /**
     * Archives a project.
     *
     * @param id The param to be archived.
     * @return A response containing the archived project and a status message.
     */
    ProjectSaveResponse archiveProject(int id);

    /**
     * Closes a param.
     * <p>
     * A param can be closed only if it has been previously archived.
     *
     * @param id The param to be closed.
     * @return A response containing the closed param and a status message.
     */
    ProjectSaveResponse closeProject(int id);

    /**
     * Checks if a project is deleted.
     *
     * @param project The project to check.
     * @return true if the project is deleted, false otherwise.
     */
    boolean isDeleted(Project project);

    /**
     * Checks if a project is closed.
     *
     * @param project The project to check.
     * @return true if the project is closed, false otherwise.
     */
    boolean isClosed(Project project);

    /**
     * Checks if a project is archived.
     *
     * @param project The project to check.
     * @return true if the project is archived, false otherwise.
     */
    boolean isArchived(Project project);

    /**
     * Save project specific taxes.
     *
     * @param projectSpecificTaxes projectSpecificTaxes to add.
     * @return A true if the specific tax is saved.      
     */
    boolean saveProjectSpecificTaxes(ProjectSpecificTaxes projectSpecificTaxes);
    
    /**
     * Retrieves all projects from a specified workspace and sorts them by user favorites.
     *
     * @param workspaceCode The code of the workspace.
     * @return A list of projects that belong to the specified workspace and are sorted by user favorites.
     */
    List<Project> getAllAndSortByLoggedUserFavorite(String workspaceCode);

    /**
     * Sorts a list of projects based on user favorites.
     *
     * @param projects  The list of projects to sort.
     * @param favorites The list of user favorites.
     * @return The sorted list of projects.
     */
    List<Project> sortByUserFavorite(List<Project> projects, List<UserProjectFavorite> favorites);
}
