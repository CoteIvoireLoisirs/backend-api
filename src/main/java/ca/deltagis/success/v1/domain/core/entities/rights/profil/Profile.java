/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-12 10:17:17
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-12 10:17:17
 */
package ca.deltagis.success.v1.domain.core.entities.rights.profil;

import ca.deltagis.success.v1.domain.core.entities.workspace.Workspace;
import ca.deltagis.success.v1.domain.ports.out.AbstractCommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "profiles")
public class Profile extends AbstractCommonEntity<Profile> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "is_default")
    private boolean isDefault;

    /**
     * workspace id
     * <li>If is null, profile is used in all application </li>
     * <li>if is not null, profile is used only in this workspace </li>
     */
    private String workspaceCode;

    @Transient
    private Workspace workspace;

    /**
     * * START REQUIRED AbstractCommonEntity
     */
    private UUID uuid; // from AbstractCommonEntity

    private Long updateBy; // from AbstractCommonEntity

    private Timestamp created; // from AbstractCommonEntity

    private Timestamp updated; // from AbstractCommonEntity

    private Timestamp deleted; // from AbstractCommonEntity

    /**
     * Retrieves a user-friendly display name of the right.
     *
     * @return the display name of the right.
     */
    @Override
    public String getDisplayName() {
        return name;
    }

    /**
     * Retrieves the ID of the user who last updated the right.
     *
     * @return the ID of the user who last updated the right.
     */
    @Override
    public Long getUpdateBy() {
        return updateBy;
    }

    /**
     * Automatically sets the timestamps for creation and update fields to the current time.
     * This method should be called when the entity is initialized or updated.
     * Note: The deletion timestamp is not set and remains unchanged.
     */
    @Override
    public void setAutoFields() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        created = currentTimestamp;
        updated = currentTimestamp;
        isDefault = false;
        uuid = UUID.randomUUID();
    }

    /**
     * Compares this right with another object for order.
     *
     * @param object the object to be compared with.
     * @return a negative integer, zero, or a positive integer as this right is
     * less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(Profile object) {
        return object.getName().compareTo(name);
    }
    /**
     * * END REQUIRED AbstractCommonEntity
     */
}
