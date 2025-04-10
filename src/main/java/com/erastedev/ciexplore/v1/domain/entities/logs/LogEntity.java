package com.erastedev.ciexplore.v1.domain.entities.logs;

import com.erastedev.ciexplore.v1.domain.entities.user.User;
import com.erastedev.ciexplore.v1.domain.entities.workspace.Workspace;
import com.erastedev.ciexplore.v1.domain.ports.out.AbstractCommonEntity;
import com.erastedev.ciexplore.v1.domain.models.AuditLogActionCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-11-06 11:56:00
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-11-06 11:56:00
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "logs")
public class LogEntity extends AbstractCommonEntity<LogEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Transient
    private User user;

    private Long workspaceId;

    @Transient
    private Workspace workspace;

    private Long projectId;

    private Timestamp actionAt;

    private String label;

    // private String description;

    private AuditLogActionCode actionCode;

    @Column(nullable = false)
    private String entityName;

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
        return label;
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
        actionAt = currentTimestamp;
        // deleted = currentTimestamp ;
    }

    @Override
    public int compareTo(LogEntity object) {
        return object.getLabel().compareTo(label);
    }

    /**
     * * END REQUIRED AbstractCommonEntity
     */

    @Override
    public String toString() {
        return "LogEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", workspaceId=" + workspaceId +
                ", projectId=" + projectId +
                ", actionAt=" + actionAt +
                ", label='" + label + '\'' +
                ", actionCode=" + actionCode +
                '}';
    }
}
