/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-25 08:51:01
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-25 08:51:01
 */
package ca.deltagis.success.v1.application.request.right;

import ca.deltagis.success.v1.domain.core.entities.rights.profil.Profile;
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
