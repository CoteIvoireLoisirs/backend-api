package com.erastedev.ciexplore.v1.domain.entities.user;

import com.erastedev.ciexplore.v1.domain.models.language.LangCodeEnum;
import com.erastedev.ciexplore.v1.domain.ports.out.AbstractCommonEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings extends AbstractCommonEntity<UserSettings> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @JsonIgnore
    private UUID uuid = UUID.randomUUID();

    /**
     * Required. Does this user have two factor authentication
     */
    private boolean twoFA;

    /**
     * Date of the user's last login.
     */
    @JsonIgnore
    private Timestamp lastLogin;

    /**
     * verifying a logged in user.
     */
    private boolean isConnected;

    /**
     * The token used for a user account restore. Will be stored as a hash.
     */
    private String restoreToken;

    /**
     * Using to store temporary token
     */
    @JsonIgnore
    private String tempToken;

    /**
     * The recovery code used for a user account restore.
     */
    private String recoveryCode;

    /**
     * The token used for a user lookup when sending restore and invite emails.
     */
    private String idToken;

    /**
     * The timestamp representing when the restore window expires.
     */
    private Date restoreExpiry;

    /**
     * Indicates whether this user is currently an invitation.
     */
    private String invitationState;

    private String gender;

    private Date birthday;

    private String nationality;

    private LangCodeEnum language;

    private String avatar;

    @Transient
    private String displayName;

    /**
     * Sets the invitation status to the specified value.
     *
     * @param invitationStatus The desired invitation status.
     */
    public void setInvitationStatus(String invitationStatus) {
        this.invitationState = invitationStatus;
    }

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
        // deleted = currentTimestamp ;
    }

    @Override
    public int compareTo(UserSettings userSettings) {
        return 1;
    }
    /**
     * * END REQUIRED AbstractCommonEntity
     */
}
