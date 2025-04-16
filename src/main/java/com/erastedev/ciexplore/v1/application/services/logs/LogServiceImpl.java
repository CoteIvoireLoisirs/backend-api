/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-06 12:13:25
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-06 12:16:09
 * @FilePath: src/main/java/ca/deltagis/success/application/services/logs/LogServiceImpl.java
 */
package com.erastedev.ciexplore.v1.application.services.logs;

import com.erastedev.ciexplore.v1.adapters.web.message.logs.LogDefaultMessage;
import com.erastedev.ciexplore.v1.application.services.auth.AuthenticationServiceImpl;
import com.erastedev.ciexplore.v1.domain.builders.LogEntityBuilder;
import com.erastedev.ciexplore.v1.domain.entities.logs.LogEntity;
import com.erastedev.ciexplore.v1.domain.entities.user.model.User;
import com.erastedev.ciexplore.v1.domain.models.AuditLogActionCode;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonRepository;
import com.erastedev.ciexplore.v1.domain.ports.in.logs.IAuditLogService;
import com.erastedev.ciexplore.v1.domain.ports.out.AbstractCommonService;
import com.erastedev.ciexplore.v1.infrastructure.repository.logs.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class LogServiceImpl<T> extends AbstractCommonService<LogEntity> implements IAuditLogService<T> {
    @Autowired
    private LogRepository repository;

    @Autowired
    AuthenticationServiceImpl userAuthService;

    Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    LogServiceImpl() {
    }

    @Override
    public List<LogEntity> findAll() {
        return repository.findAll();
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
    public LogEntity save(LogEntity entity) {
        return repository.save(entity);
    }

    @Override
    public ICommonRepository<LogEntity> getRepository() {
        return repository;
    }

    /**
     * Custom methods ********************************************************************
     * ***********************************************************************************
     */

    /**
     * Create a log for the given user based on the given message and description.
     *
     * @param message     the message to log
     * @param description the description of the action
     * @param user        the user who performed the action
     */
    public void setLogForAuthAction(AuditLogActionCode action, LogDefaultMessage message, String description, User user) {
        try {
            User loggedUser = userAuthService.getCurrentLoggedUser();
            LogEntity log = LogEntityBuilder
                    .builder(action, user, null)
                    .userId(user != null && user.getId() != null ? user.getId() : null)
                    .workspaceId(null)
                    .projectId(null)
                    .actionAt(new Timestamp(System.currentTimeMillis()))
                    .label(message.getMessage())
                    .updateBy(loggedUser.getId())
                    .build();

            logger.info("Log created: label : {}, description : {}, user : {}, workspace : {}, project : {}, actionAt : {}, updateBy : {}", log.getLabel(), log.getUserId(), log.getWorkspaceId(), log.getProjectId(), log.getActionAt(), log.getUpdateBy());
            repository.save(log);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Methos for IAuditLogService *******************************************************
     * ***********************************************************************************
     */

    /**
     * Creates a log entry for the given user, action code, and message.
     *
     * @param actionCode the action code representing the type of action performed
     * @param message    a brief description of the action
     * @param user       the user who performed the action
     * @param t          the object that was the target of the action
     */
    public void setLog(AuditLogActionCode actionCode, String message, User user, T t) {
        LogEntity logEntry = new LogEntity();
        logEntry.setEntityName(t.getClass().getSimpleName());
        logEntry.setActionCode(actionCode);
        logEntry.setLabel(message);
        logEntry.setUser(user);
        logEntry.setAutoFields();
        logger.info("Log entry created: {}", logEntry.toString());
        repository.save(logEntry);
    }

    public User user() {
        return userAuthService.getCurrentLoggedUser();
    }

    public void logCreate(T entity, Boolean isSuccess) {
        if (entity == null) {
            logger.error("Entity is null. Cannot log create action.");
            return;
        }

        AuditLogActionCode action = !isSuccess ? AuditLogActionCode.CREATE_FAILED : AuditLogActionCode.CREATE_SUCCESS;
        String entityName = entity.getClass().getSimpleName();
        String message = (isSuccess ? "Created " : "Failed to create ") + entityName;

        try {
            setLog(action, message, user(), entity);
        } catch (Exception e) {
            logger.error("Error logging create action: {}", e.toString());
        }
    }

    public void logRead(T entity, Boolean isSuccess) {
        if (entity == null) {
            logger.error("Entity is null. Cannot log read action.");
            return;
        }

        AuditLogActionCode action = isSuccess ? AuditLogActionCode.READ_SUCCESS : AuditLogActionCode.READ_FAILED;
        String entityName = entity.getClass().getSimpleName();
        String message = (isSuccess ? "Read " : "Failed to read ") + entityName;

        try {
            setLog(action, message, user(), entity);
        } catch (Exception e) {
            logger.error("Error logging read action: {}", e.toString());
        }
    }

    public void logUpdate(T entity, Boolean isSuccess) {
        if (entity == null) {
            logger.error("Entity is null. Cannot log update action.");
            return;
        }

        AuditLogActionCode action = !isSuccess ? AuditLogActionCode.UPDATE_FAILED : AuditLogActionCode.UPDATE_SUCCESS;
        String entityName = entity.getClass().getSimpleName();
        String message = (isSuccess ? "Updated " : "Failed to update ") + entityName;

        try {
            setLog(action, message, user(), entity);
        } catch (Exception e) {
            logger.error("Error logging update action: {}", e.toString());
        }
    }

    public void logDelete(T entity, Boolean isSuccess, String message) {
        if (entity == null) {
            logger.error("Entity is null. Cannot log delete action.");
            return;
        }

        if (message == null || message.trim().isEmpty()) {
            logger.error("Message is null or empty. Cannot log delete action.");
            return;
        }

        AuditLogActionCode action = !isSuccess ? AuditLogActionCode.DELETE_FAILED : AuditLogActionCode.DELETE_SUCCESS;

        try {
            setLog(action, message, user(), entity);
        } catch (Exception e) {
            logger.error("Error logging delete action: {}", e.toString());
        }
    }
}
