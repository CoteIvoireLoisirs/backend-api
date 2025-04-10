package ca.deltagis.success.v1.application.services.project;

import ca.deltagis.success.v1.application.services.user.UserAuthServiceImpl;
import ca.deltagis.success.v1.domain.core.entities.project.UserProjectFavorite;
import ca.deltagis.success.v1.domain.ports.in.ICommonRepository;
import ca.deltagis.success.v1.domain.ports.in.project.IUserProjectFavorite;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonService;
import ca.deltagis.success.v1.infrastructure.repository.project.UserProjectFavoriteRepository;
import ca.deltagis.success.v1.infrastructure.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserProjectFavoriteImpl extends AbstractCommonService<UserProjectFavorite> implements IUserProjectFavorite {

    @Autowired
    private UserProjectFavoriteRepository repository;

    @Autowired
    private UserAuthServiceImpl authService;

    Logger logger = LoggerFactory.getLogger(UserProjectFavoriteImpl.class);

    @Override
    public List<UserProjectFavorite> findAll() {
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
    public UserProjectFavorite save(UserProjectFavorite entity) {
        try {
            if (entity.getId() != null) {
                entity.setAutoFields();
            }
            entity.setUpdateBy(authService.getCurrentLoggedUser().getId());
            entity.setUpdated(DateUtil.getCurrentTimestamp());
            return repository.save(entity);
        } catch (Exception e) {
            logger.error("Error saving entity", e);
            return null;
        }
    }

    @Override
    public ICommonRepository<UserProjectFavorite> getRepository() {
        return repository;
    }

    /**
     * Retrieves a list of UserProjectFavorites for the given user ID.
     *
     * @param userId the ID of the user
     * @return a list of UserProjectFavorites
     */
    @Override
    public List<UserProjectFavorite> getAllByUserId(Long userId) {
        try {
            return repository.findByUserId(userId);
        } catch (Exception e) {
            logger.info("Failed to retrieve user project favorites for user ID {}: {}", userId, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves a list of UserProjectFavorites for the given project ID.
     *
     * @param projectId the ID of the project
     * @return a list of UserProjectFavorites
     */
    @Override
    public List<UserProjectFavorite> getAllByProjectId(Long projectId) {
        try {
            return repository.findByProjectId(projectId);
        } catch (Exception e) {
            logger.info("Failed to retrieve user project favorites for project ID {}: {}", projectId, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Saves or deletes a UserProjectFavorite depending on whether it already exists
     *
     * @param userProjectFavorite the UserProjectFavorite to be saved or deleted
     */
    @Override
    public UserProjectFavorite saveOrDelete(UserProjectFavorite userProjectFavorite) {
        try {
            UserProjectFavorite checkExists = getByProjectIdAndUserId(userProjectFavorite.getProjectId(), userProjectFavorite.getUserId());
            // if exist check deleted
            if (checkExists != null) {
                if (checkExists.getDeleted() == null) {
                    deleteById(checkExists.getId());
                    return null;
                } else {
                    checkExists.setDeleted(null);
                    return save(checkExists);
                }
            }

            // save if not exist
            return save(userProjectFavorite);
        } catch (Exception e) {
            logger.info("Failed to save or delete user project favorite: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves a UserProjectFavorite for the given user ID and project ID.
     *
     * @param userId    the ID of the user
     * @param projectId the ID of the project
     * @return a UserProjectFavorite if found, otherwise null
     */
    @Override
    public UserProjectFavorite getByProjectIdAndUserId(Long projectId, Long userId) {
        try {
            return repository.findByProjectIdAndUserId(projectId, userId);
        } catch (Exception e) {
            logger.info("Failed to retrieve user project favorite for project ID {} and user ID {}: {}", projectId, userId, e.getMessage());
            return null;
        }
    }
}
