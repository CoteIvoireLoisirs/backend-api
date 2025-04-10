package ca.deltagis.success.v1.domain.ports.in.project;

import ca.deltagis.success.v1.domain.core.entities.project.UserProjectFavorite;

import java.util.List;

public interface IUserProjectFavorite {
    /**
     * Retrieves a list of UserProjectFavorites for the given user ID.
     *
     * @param userId the ID of the user
     * @return a list of UserProjectFavorites
     */
    List<UserProjectFavorite> getAllByUserId(Long userId);

    /**
     * Retrieves a list of UserProjectFavorites for the given project ID.
     *
     * @param projectId the ID of the project
     * @return a list of UserProjectFavorites
     */
    List<UserProjectFavorite> getAllByProjectId(Long projectId);

    /**
     * Saves or deletes a UserProjectFavorite depending on whether it already exists
     *
     * @param userProjectFavorite the UserProjectFavorite to be saved or deleted
     */
    UserProjectFavorite saveOrDelete(UserProjectFavorite userProjectFavorite);

    /**
     * Retrieves a UserProjectFavorite for the given user ID and project ID.
     *
     * @param userId    the ID of the user
     * @param projectId the ID of the project
     * @return a UserProjectFavorite if found, otherwise null
     */
    UserProjectFavorite getByProjectIdAndUserId(Long projectId, Long userId);
}
