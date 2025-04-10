/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-10 15:06:10
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-10 15:12:03
 * @FilePath: src/main/java/ca/deltagis/success/v1/adapters/web/http/user/request/CreateUserFromWorkspaceRequest.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package ca.deltagis.success.v1.application.request.user;

import ca.deltagis.success.v1.adapters.web.message.user.UserCustomMessage;
import ca.deltagis.success.v1.domain.core.entities.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserFromWorkspaceRequest {
    User user;
    
    String workspaceCode;

    UserCustomMessage error;
}
