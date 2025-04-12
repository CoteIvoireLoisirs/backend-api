/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-12 11:40:53
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-12 11:40:53
 */
package com.erastedev.ciexplore.v1.domain.ports.in.workspace;

import com.erastedev.ciexplore.v1.application.request.workspace.WorkspaceSaveResponse;
import com.erastedev.ciexplore.v1.domain.entities.workspace.Workspace;
import com.erastedev.ciexplore.v1.domain.models.FileUploadResponse;
import com.erastedev.ciexplore.v1.domain.models.workspace.WorkspacePublic;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IWorkspaceService {
    /**
     * Retrieves a workspace by its code.
     *
     * @param code the code of the workspace to be retrieved.
     * @return an Optional containing the workspace if found, or empty if not found.
     */
    Optional<Workspace> getByCode(String code);

    /**
     * Retrieves a list of all workspaces.
     *
     * @return a list containing all workspaces.
     */
    List<WorkspacePublic> getWorkspaceWithOutAuthorization();

    /**
     * Saves a workspace.
     *
     * @param workspace The workspace to be saved.
     * @return a response containing the workspace if saved successfully, or an error message if not.
     */
    WorkspaceSaveResponse saveWorkspace(Workspace workspace);

    /**
     * Activates a workspace by its code.
     *
     * @param code the code of the workspace to be activated.
     * @return a response containing the workspace if activated successfully, or an error message if not.
     */
    WorkspaceSaveResponse activeWorkspace(String code);

    /**
     * Disables a workspace by its code.
     *
     * @param code the code of the workspace to be disabled.
     * @return a response containing the workspace if disabled successfully, or an error message if not.
     */
    WorkspaceSaveResponse disableWorkspace(String code);

    /**
     * Updates the image of a workspace.
     *
     * @param file          The image file to be updated.
     * @param workspaceCode The code of the workspace to be updated.
     * @return a URL to the updated image.
     */
    FileUploadResponse changeWorkspaceImage(MultipartFile file, String workspaceCode);

    /**
     * Updates the image of a workspace.
     *
     * @param file          The image file to be updated.
     * @param workspaceCode The code of the workspace to be updated.
     * @return The updated workspace.
     */
    WorkspaceSaveResponse updateWorkspaceImage(MultipartFile file, String workspaceCode);

    /**
     * Retrieves the directory for the workspace image files.
     *
     * @param workspaceCode The code of the workspace.
     * @return The directory for the workspace image files.
     */
    String getDirectory(String workspaceCode);

    /**
     * Counts the number of projects in a workspace by its code.
     *
     * @param workspaceCode the code of the workspace.
     * @return the number of projects in the workspace.
     */
    int countProjectByWorkspaceCode(String workspaceCode);
}
