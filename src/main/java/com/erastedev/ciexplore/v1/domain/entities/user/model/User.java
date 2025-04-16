package com.erastedev.ciexplore.v1.domain.entities.user.model;

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
    public int compareTo(User user) {
        return 0;
    }
    /**
     * * END REQUIRED AbstractCommonEntity
     */
}
