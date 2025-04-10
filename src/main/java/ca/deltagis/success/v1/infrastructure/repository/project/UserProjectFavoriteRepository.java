package ca.deltagis.success.v1.infrastructure.repository.project;

import ca.deltagis.success.v1.domain.core.entities.project.UserProjectFavorite;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProjectFavoriteRepository extends ICommonRepository<UserProjectFavorite> {
    /**
     * Retrieves a UserProjectFavorite for the given user ID and project ID.
     *
     * @param userId    the ID of the user
     * @param projectId the ID of the project
     * @return a UserProjectFavorite if found, otherwise null
     */
    UserProjectFavorite findByProjectIdAndUserId(Long projectId, Long userId);

    /**
     * Retrieves a list of UserProjectFavorites for the given user ID.
     *
     * @param userId the ID of the user
     * @return a list of UserProjectFavorites
     */
    List<UserProjectFavorite> findByUserId(Long userId);

    /**
     * Retrieves a list of UserProjectFavorites for the given project ID.
     *
     * @param projectId the ID of the project
     * @return a list of UserProjectFavorites
     */
    List<UserProjectFavorite> findByProjectId(Long projectId);
}
