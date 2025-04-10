/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-11 17:20:49
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-11 17:21:05
 * @FilePath: src/main/java/ca/deltagis/success/v1/domain/core/models/user/UserDeleteResponse.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package com.erastedev.ciexplore.v1.domain.models.user;

import com.erastedev.ciexplore.v1.domain.entities.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDeleteResponse {
    private final User user;
    
    private UserRegisterState state;
    
    private final Boolean success;
}
