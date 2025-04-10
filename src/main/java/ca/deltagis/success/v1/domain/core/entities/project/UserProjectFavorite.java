/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-12-11 19:06:38
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-11 19:35:43
 * @FilePath: src/main/java/ca/deltagis/success/v1/domain/core/entities/project/UserProjectFavorite.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package ca.deltagis.success.v1.domain.core.entities.project;

import ca.deltagis.success.v1.domain.ports.out.AbstractCommonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Table(
        name = "user_project_favorite",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "project_id"})
        }
)
@Entity
public class UserProjectFavorite extends AbstractCommonEntity<UserProjectFavorite> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long projectId;

    /**
     * * START REQUIRED AbstractCommonEntity
     */

    private UUID uuid; // from AbstractCommonEntity

    private Long updateBy; // from AbstractCommonEntity

    private Timestamp created; // from AbstractCommonEntity

    private Timestamp updated; // from AbstractCommonEntity

    private Timestamp deleted; // from AbstractCommonEntity

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public void setAutoFields() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        created = currentTimestamp;
        updated = currentTimestamp;
        uuid = UUID.randomUUID();
    }

    @Override
    public int compareTo(UserProjectFavorite object) {
        return object.getUserId().compareTo(userId);
    }
    /**
     * * END REQUIRED AbstractCommonEntity
     */
}
