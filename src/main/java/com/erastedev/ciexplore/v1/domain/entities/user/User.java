package com.erastedev.ciexplore.v1.domain.entities.user;

import com.erastedev.ciexplore.v1.domain.models.language.LangCodeEnum;
import com.erastedev.ciexplore.v1.domain.ports.out.AbstractCommonEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.*;

//@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends AbstractCommonEntity<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @JsonIgnore
    private UUID uuid = UUID.randomUUID();

    @Column(unique = true)
    private String username;

    /**
     * Required. Will be stored as a hash.
     */
    private String password;

    /**
     * Required. Does this user have two factor authentication
     */
    private boolean twoFA;

    /**
     * Date when password was changed.
     */
    @JsonIgnore
    private Date passwordLastUpdated;

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

    /**
     * Indicates whether this is user is disabled, which means the user cannot
     * be authenticated.
     */
    @Column(name = "disabled", columnDefinition = "boolean default false")
    private boolean disabled = false;

    private String surname;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    private String jobTitle;

    private String introduction;

    private String gender;

    private Date birthday;

    private String nationality;

    private String employer;

    private String education;

    private String interests;

    private LangCodeEnum language;

    private String avatar;

    private boolean isAdmin;

    // @JsonIgnore
    // private int roleId;

    // @Transient
    // private Right role;

    @Transient
    private UserProfile currentUserProfile;

    @Transient
    private String displayName;

    public void setDisplayName(String displayName) {
        this.displayName = this.getFirstName() + " " + this.getLastName();
    }

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
        return (firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName);
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
    public int compareTo(User object) {
        return object.getUsername().compareTo(username);
    }
    /**
     * * END REQUIRED AbstractCommonEntity
     */
}
