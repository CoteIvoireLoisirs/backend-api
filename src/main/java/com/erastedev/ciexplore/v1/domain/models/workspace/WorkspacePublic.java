package com.erastedev.ciexplore.v1.domain.models.workspace;

import com.erastedev.ciexplore.v1.domain.entities.workspace.Workspace;
import lombok.Data;

import java.util.UUID;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-03 17:32:40
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:46
 * @FilePath: src/main/java/ca/deltagis/success/v1/domain/core/models/workspace/WorkspacePublic.java
 * @Description: Mapping between Workspace and WorkspacePublic
 */
@Data
public class WorkspacePublic {
    Long id;
    UUID uuid;
    String name;
    String code;
    // Date limitTime; TODO : check if necessary

    /**
     * Workspace identifier
     */
    public static WorkspacePublic buildFromWorkspace(Workspace workspace) {
        WorkspacePublic workspacePublic = new WorkspacePublic();
        workspacePublic.setId(workspace.getId());
        workspacePublic.setUuid(workspace.getUuid());
        workspacePublic.setName(workspace.getName());
        workspacePublic.setCode(workspace.getCode());
        // workspacePublic.setLimitTime(workspace.getLimitTime());
        return workspacePublic;
    }
}
