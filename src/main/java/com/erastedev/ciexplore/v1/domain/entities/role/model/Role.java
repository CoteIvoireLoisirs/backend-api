package com.erastedev.ciexplore.v1.domain.entities.role.model;

import com.erastedev.ciexplore.v1.domain.ports.out.AbstractCommonEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class UserRole extends AbstractCommonEntity<UserRole> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @JsonIgnore
    private UUID uuid = UUID.randomUUID();

    @Column(unique = true)
    private String name;

    private boolean enable = true;

    /**
     * * START REQUIRED AbstractCommonEntity
     */

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
    public int compareTo(UserRole role) {
        return 0;
    }
    /**
     * * END REQUIRED AbstractCommonEntity
     */
}
