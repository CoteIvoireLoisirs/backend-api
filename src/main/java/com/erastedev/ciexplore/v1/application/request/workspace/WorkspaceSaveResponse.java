/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-12 12:20:47
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-12 12:20:58
 */
package com.erastedev.ciexplore.v1.application.request.workspace;

import com.erastedev.ciexplore.v1.adapters.web.message.WorkspaceMessage;
import com.erastedev.ciexplore.v1.domain.entities.workspace.Workspace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceSaveResponse {
    private Workspace workspace;
    private WorkspaceMessage error;
    private HashMap<String, String> errorDetail = null;
}
