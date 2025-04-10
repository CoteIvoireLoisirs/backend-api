/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-28 09:32:50
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-28 09:32:50
 */
package ca.deltagis.success.v1.domain.core.entities.user;

import ca.deltagis.success.v1.domain.core.entities.rights.ModuleProfileRight;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonEntity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(
        name = "user_profile",
        uniqueConstraints = @UniqueConstraint(
                name = "user_profile_unique_constraint",
                columnNames = {"userId", "workspaceCode"}
        )
)
public class UserProfile extends AbstractCommonEntity<UserProfile> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long moduleProfileRightId;

    @Transient
    private ModuleProfileRight right;

    private String workspaceCode;

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
        return "";
    }

    @Override
    public Long getUpdateBy() {
        return updateBy;
    }

    @Override
    public void setAutoFields() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        created = currentTimestamp;
        updated = currentTimestamp;
        uuid = UUID.randomUUID();
    }

    @Override
    public int compareTo(UserProfile object) {
        return 1;
    }
    /**
     * * END REQUIRED AbstractCommonEntity
     */
}
