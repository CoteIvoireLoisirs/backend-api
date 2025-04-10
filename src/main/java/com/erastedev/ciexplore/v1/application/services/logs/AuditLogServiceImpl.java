/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-06 20:40:51
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-14 15:05:56
 */
package com.erastedev.ciexplore.v1.application.services.logs;

import com.erastedev.ciexplore.v1.domain.entities.logs.LogEntity;
import com.erastedev.ciexplore.v1.domain.entities.user.User;
import com.erastedev.ciexplore.v1.domain.models.AuditLogActionCode;
import com.erastedev.ciexplore.v1.domain.ports.in.logs.IAuditLogService;
import com.erastedev.ciexplore.v1.infrastructure.repository.logs.LogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class AuditLogServiceImpl<T> implements IAuditLogService<T> {
    @Autowired
    private LogRepository logRepository;

    Logger logger = LoggerFactory.getLogger(AuditLogServiceImpl.class);

    public void setLog(AuditLogActionCode actionCode, String message, User user, T t) {
        LogEntity logEntry = new LogEntity();
        logEntry.setEntityName(t.getClass().getSimpleName());
        logEntry.setActionCode(actionCode);
        logEntry.setLabel(message);
        logEntry.setUser(user);
        logEntry.setAutoFields();
        logger.info("Log entry created: {}", logEntry.toString());
        logRepository.save(logEntry);
    }

    public void logCreate(String message, User user, T entity, Boolean isFailed) {
        AuditLogActionCode action = isFailed ? AuditLogActionCode.CREATE_FAILED : AuditLogActionCode.CREATE_SUCCESS;
        setLog(action, message, user, entity);
    }

    public void logRead(String message, User user, T entity, Boolean isFailed) {
        AuditLogActionCode action = isFailed ? AuditLogActionCode.READ_FAILED : AuditLogActionCode.READ_SUCCESS;
        setLog(action, message, user, entity);
    }

    public void logUpdate(String message, User user, T entity, Boolean isFailed) {
        AuditLogActionCode action = isFailed ? AuditLogActionCode.UPDATE_FAILED : AuditLogActionCode.UPDATE_SUCCESS;
        setLog(action, message, user, entity);
    }

    public void logDelete(String message, User user, T entity, Boolean isFailed) {
        AuditLogActionCode action = isFailed ? AuditLogActionCode.DELETE_FAILED : AuditLogActionCode.DELETE_SUCCESS;
        setLog(action, message, user, entity);
    }
}
