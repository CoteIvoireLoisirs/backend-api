package com.erastedev.ciexplore.v1.domain.ports.out;

import com.erastedev.ciexplore.v1.application.services.user.UserServiceImpl;
import com.erastedev.ciexplore.v1.domain.entities.user.User;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonEntity;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonService;
import com.erastedev.ciexplore.v1.domain.ports.in.logs.Logs;
import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract service implementation for common entity operations.
 * Provides basic CRUD operations and user context management.
 *
 * @param <T> the type of the entity.
 */
public abstract class AbstractCommonService<T extends ICommonEntity<T>> implements ICommonService<T> {

    private static final Log log = LogFactory.getLog(AbstractCommonService.class);
    protected final Logger logger = Logger.getLogger(AbstractCommonService.class.getName());

    private String username = "system-process";
    private User currentUser = new User();

    @Autowired
    private UserServiceImpl userService;

    public Class<T> getClassName() {
        Type[] actualTypeArguments = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        return (Class<T>) actualTypeArguments[0];
    }

    @Override
    public T getById(Long id) {
        return getByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with ID " + id + " not found"));
    }

    @Override
    public Optional<T> getByIdOptional(Long id) {
        return getRepository().findByIdAndDeletedIsNull(id);
    }

    // @Override
    public Optional<T> getByIdOptional(long idRecord) {
        return getRepository().findByIdAndDeletedIsNull(idRecord);
    }

    @Override
    public List<T> getAll() {
        // Sort.by(Sort.Direction.DESC, "id")
        return getRepository().findAllByDeletedIsNull();
    }

    @Override
    public T create(T entity) {
        setCurrentUser();
        entity.setUpdateBy(currentUser.getId());
        return create(entity, currentUser);
    }

    public T create(T entity, User user) {
        setCurrentUser();
        username = user.getDisplayName();

        entity.setCreated(new Timestamp(System.currentTimeMillis()));
        entity.setUpdated(new Timestamp(System.currentTimeMillis()));
        entity.setUpdateBy(user.getId());

        try {
            T savedEntity = getRepository().save(entity);
            Logs.infoWrapper(log, username, savedEntity, Logs.ACTION_CREATE);
            return savedEntity;
        } catch (Exception e) {
            log.error("Error creating entity", e);
            Logs.infoWrapper(log, username, e.getMessage(), Logs.ACTION_CREATE_DENIED);
            throw new ServiceException("Failed to create entity", e);
        }
    }

    @Override
    public T update(T entity) {
        setCurrentUser();
        entity.setUpdateBy(currentUser.getId());
        return update(entity, currentUser);
    }

    @Override
    public T update(T entity, User user) {
        setCurrentUser();
        username = user.getDisplayName();
        entity.setUpdateBy(user.getId());
        entity.setUpdated(new Timestamp(System.currentTimeMillis()));

        if (getByIdOptional(entity.getId()).isEmpty()) {
            throw new EntityNotFoundException("Entity with ID " + entity.getId() + " not found for update");
        }

        try {
            T updatedEntity = getRepository().save(entity);
            Logs.infoWrapper(log, username, updatedEntity, Logs.ACTION_UPDATE);
            return updatedEntity;
        } catch (Exception e) {
            log.error("Error updating entity", e);
            Logs.infoWrapper(log, username, e.getMessage(), Logs.ACTION_UPDATE_DENIED);
            throw new ServiceException("Failed to update entity", e);
        }
    }

    @Override
    public String delete(T entity) {
        return delete(entity, currentUser);
    }

    @Override
    public String delete(T entity, User user) {
        setCurrentUser();
        username = user.getDisplayName();
        entity.setUpdateBy(user.getId());
        entity.setDeleted(new Timestamp(System.currentTimeMillis()));

        try {
            getRepository().save(entity);
            Logs.infoWrapper(log, username, entity, Logs.ACTION_DELETE);
            return "Entity successfully deleted";
        } catch (Exception e) {
            log.error("Error deleting entity", e);
            Logs.infoWrapper(log, username, e.getMessage(), Logs.ACTION_DELETE_DENIED);
            throw new ServiceException("Failed to delete entity", e);
        }
    }

    /**
     * Sets the current user and updates the username for logging.
     */
    public void setCurrentUser() {
        this.currentUser = getCurrentUser();
        this.username = currentUser.getDisplayName();
    }

    /**
     * Retrieves the current user based on the security context.
     *
     * @return the current user or a default user if not authenticated.
     */
    protected User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken || !authentication.isAuthenticated()) {
            logger.log(Level.WARNING, "Anonymous user accessing the service");
            return new User();
        }

        String userEmail = authentication.getName();
        return userService.getOptionalUserByEmail(userEmail)
                .orElseGet(() -> {
                    logger.log(Level.WARNING, "User with email " + userEmail + " not found");
                    return new User();
                });
    }
}
