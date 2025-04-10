package com.erastedev.ciexplore.v1.domain.entities.user;

import com.erastedev.ciexplore.v1.domain.entities.rights.GrantedRight;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRole {
    public static final String AUTHORITY_ALL = "ALL";

    public static final String AUTHORITY_STANDARD = "STANDARD";

    public static final String AUTHORITY_VIEWER = "VIEWER";

    private String description;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private UUID uid;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> authorities = new HashSet<>();

    // @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    @Transient
    private Set<GrantedRight> grantedRights;

    private Long projectId;

    /**
     * Indicates whether this user has super-user access.
     *
     * @return true if this user has super-user access, false otherwise
     */
    @JsonProperty
    public boolean isSuper() {
        return authorities != null && authorities.contains(AUTHORITY_ALL);
    }

    /**
     * Indicates whether this user has standard access.
     *
     * @return true if this user has standard access, false otherwise
     */
    @JsonProperty
    public boolean isStandard() {
        return authorities != null && authorities.contains(AUTHORITY_STANDARD);
    }

    /**
     * Indicates whether this user has viewer access.
     *
     * @return true if this user has viewer access, false otherwise
     */
    @JsonProperty
    public boolean isViewer() {
        return authorities != null && authorities.contains(AUTHORITY_VIEWER);
    }

    /**
     * Adds a user to the granted rights for this role.
     * <p>
     * Does nothing if the user already has rights for this role in the given project.
     *
     * @param user    the user to add
     */
    public void addUser(User user) {
//        GrantedRight right = new GrantedRight();
//        right.setRole(this);
//        right.setUser(user);
//
//        List<GrantedRight> rights = new ArrayList<GrantedRight>();
//
//
//        grantedRights.add(right);
    }

    public void removeUser(User user) {
        grantedRights = grantedRights.stream().filter(g -> g.getUser() == user)
                .collect(Collectors.toSet());
    }


    public Set<User> getMembers() {
        return grantedRights.stream().map(GrantedRight::getUser).collect(Collectors.toSet());
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();

        for (User user : getMembers()) {
            if (user != null) {
                users.add(user);
            }
        }

        return users;
    }

    public static UserRole createDefaultRole() {
        UserRole userRole = new UserRole();

        // TODO : fix permissions
        // userRole.setName(CodeGenerator.generateCode(12));
        // userRole.setAuthorities(Sets.newHashSet(AUTHORITY_ALL));
        // userRole.setCode(CodeGenerator.generateCode(20));

        return userRole;
    }

}
