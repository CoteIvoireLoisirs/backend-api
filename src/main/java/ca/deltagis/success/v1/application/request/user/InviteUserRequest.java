/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-24 11:35:48
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-10-28 16:02:07
 */
package ca.deltagis.success.v1.application.request.user;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteUserRequest {
    private String email;
    private int roleId;
    private String host;
}
