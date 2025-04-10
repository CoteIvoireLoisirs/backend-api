/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-12 13:17:48
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-12 13:17:48
 */
package com.erastedev.ciexplore.v1.domain.entities.rights;

import com.erastedev.ciexplore.v1.domain.entities.rights.profil.Profile;
import com.erastedev.ciexplore.v1.domain.ports.out.AbstractCommonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "module_profile_right")
public class ModuleProfileRight extends AbstractCommonEntity<ModuleProfileRight> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Store as "moduleID_RWD;[]"
     */
    @Column(nullable = false)
    private String rights;

    @Transient
    private HashMap<String, HashMap<String, Boolean>> moduleRight;

    @Column(nullable = false)
    private Long profileId;

    @Transient
    private Profile profile;

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
        return rights;
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
    public int compareTo(ModuleProfileRight object) {
        return object.getRights().compareTo(rights);
    }
    /**
     * * END REQUIRED AbstractCommonEntity
     */
}
