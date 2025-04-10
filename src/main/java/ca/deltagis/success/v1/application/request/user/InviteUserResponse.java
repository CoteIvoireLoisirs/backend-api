/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-24 11:51:35
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:45
 * @FilePath: src/main/java/ca/deltagis/success/v1/adapters/web/http/user/request/InviteUserResponse.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package ca.deltagis.success.v1.application.request.user;


import ca.deltagis.success.v1.domain.core.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InviteUserResponse {
    private String link;
    private boolean emailSent;
    private User user;
    private InviteUserState invited;
}
