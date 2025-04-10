/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-13 09:27:56
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-13 09:27:56
 */
package ca.deltagis.success.v1.adapters.web.http.project.request;

import ca.deltagis.success.v1.adapters.web.message.ProjectMessage;
import ca.deltagis.success.v1.domain.core.entities.project.Project;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class ProjectsByWorkspaceResponse {
    List<Project> projects = new ArrayList<>();

    ProjectMessage error = null;
}
