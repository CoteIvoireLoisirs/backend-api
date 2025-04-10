package com.erastedev.ciexplore.v1.domain.entities.workspace;

import com.erastedev.ciexplore.v1.domain.entities.user.User;
import com.erastedev.ciexplore.v1.domain.ports.out.AbstractCommonEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * @author Serge BROU <bkacou@omcprojets.com, brouserge1er@gmail.com>
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "workspaces")
public class Workspace extends AbstractCommonEntity<Workspace> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private int limitUser;

    private int limitProject;

    @Transient
    private int projectCount = 0;

    @Transient
    private int userCount = 0;

    private Date limitTime;

    private String description;

    private String imagePath = null;

    @Transient
    String imageUrl = null;

    @Enumerated(EnumType.STRING)
    private WorkSpaceStatus status = WorkSpaceStatus.PENDING;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @JsonProperty
    @Transient
    private User owner;

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
        return name;
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
        // deleted = currentTimestamp ;
    }

    @Override
    public int compareTo(Workspace object) {
        return object.getName().compareTo(name);
    }
    /**
     * * END REQUIRED AbstractCommonEntity
     */
}
