/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-28 10:07:15
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-05 09:29:46
 * @FilePath: src/main/java/ca/deltagis/success/v1/adapters/web/http/user/request/SaveUserProfileResponse.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package com.erastedev.ciexplore.v1.application.request.user;

import com.erastedev.ciexplore.v1.adapters.web.message.user.UserProfileError;
import com.erastedev.ciexplore.v1.domain.entities.user.UserProfile;
import lombok.*;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveUserProfileResponse {
    List<UserProfile> userProfiles;
    UserProfileError error;
    HashMap<String, String> errors = new HashMap<>();
}
