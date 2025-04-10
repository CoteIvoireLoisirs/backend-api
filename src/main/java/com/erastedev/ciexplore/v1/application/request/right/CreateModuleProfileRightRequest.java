/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-25 08:51:01
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-25 08:51:01
 */
package com.erastedev.ciexplore.v1.application.request.right;

import com.erastedev.ciexplore.v1.domain.entities.rights.profil.Profile;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateModuleProfileRightRequest {
    String id;

    Profile profile;

    /**
     * moduleId + "_" + "rwd";
     */
    String rights; // HashMap<Integer, String>
}
