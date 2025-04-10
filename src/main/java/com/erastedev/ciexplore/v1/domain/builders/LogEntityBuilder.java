/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-06 13:12:21
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-06 15:33:58
 */
package com.erastedev.ciexplore.v1.domain.builders;

import com.erastedev.ciexplore.v1.domain.entities.logs.LogEntity;
import com.erastedev.ciexplore.v1.domain.entities.user.User;
import com.erastedev.ciexplore.v1.domain.entities.workspace.Workspace;
import com.erastedev.ciexplore.v1.domain.models.AuditLogActionCode;

import java.sql.Timestamp;

public class LogEntityBuilder {
    private Long userId;
    private Long workspaceId;
    private Long projectId;
    private Timestamp actionAt;
    private String label;
    private Long updateBy;
    private AuditLogActionCode actionCode;

    private LogEntityBuilder(AuditLogActionCode actionCode, User user, Workspace workspace) {
        this.actionCode = actionCode;
        this.userId = user != null ? user.getId() : null;
        this.workspaceId = workspace != null ? workspace.getId() : null;
    }

    /**
     * Returns a new {@link LogEntityBuilder} instance for the given {@link AuditLogActionCode},
     * {@link User}, {@link Workspace}.
     *
     * @param actionCode the action code to be used in the log entry
     * @param user       the user who triggered the action
     * @param workspace  the workspace in which the action took place
     * @return a new {@link LogEntityBuilder} instance
     */
    public static LogEntityBuilder builder(AuditLogActionCode actionCode, User user, Workspace workspace) {
        return new LogEntityBuilder(actionCode, user, workspace);
    }

    /**
     * Sets the user ID to be used in the log entry.
     *
     * @param userId the ID of the user who triggered the action
     * @return the builder instance
     */
    public LogEntityBuilder userId(Long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Sets the workspace ID to be used in the log entry.
     *
     * @param workspaceId the ID of the workspace in which the action took place
     * @return the builder instance
     */
    public LogEntityBuilder workspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
        return this;
    }

    /**
     * Sets the project ID to be used in the log entry.
     *
     * @param projectId the ID of the project in which the action took place
     * @return the builder instance
     */
    public LogEntityBuilder projectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    /**
     * Sets the timestamp at which the action took place to be used in the log entry.
     *
     * @param actionAt the timestamp of the action
     * @return the builder instance
     */
    public LogEntityBuilder actionAt(Timestamp actionAt) {
        this.actionAt = actionAt;
        return this;
    }

    /**
     * Sets the label to be used in the log entry.
     *
     * @param label a brief description of the action
     * @return the builder instance
     */
    public LogEntityBuilder label(String label) {
        this.label = label;
        return this;
    }

    /**
     * Sets the ID of the user who last updated the entity to be used in the log entry.
     *
     * @param updateBy the ID of the user who last updated the entity
     * @return the builder instance
     */
    public LogEntityBuilder updateBy(Long updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    /**
     * Sets the action code to be used in the log entry.
     *
     * @param actionCode the action code representing the type of action performed
     * @return the builder instance
     */
    public LogEntityBuilder actionCode(AuditLogActionCode actionCode) {
        this.actionCode = actionCode;
        return this;
    }

    /**
     * Creates a new {@link LogEntity} instance from the builder.
     *
     * <p>
     * This method sets the fields of the entity to the values provided to the
     * builder, and then sets the timestamps as defined in the entity.
     *
     * @return a new {@link LogEntity} instance
     */
    public LogEntity build() {
        LogEntity logEntity = new LogEntity();
        logEntity.setUserId(this.userId);
        logEntity.setWorkspaceId(this.workspaceId);
        logEntity.setProjectId(this.projectId);
        logEntity.setActionAt(this.actionAt);
        logEntity.setLabel(this.label);
        logEntity.setUpdateBy(this.updateBy);
        logEntity.setActionCode(this.actionCode);
        logEntity.setAutoFields(); // Set timestamps as defined in the entity
        return logEntity;
    }
}