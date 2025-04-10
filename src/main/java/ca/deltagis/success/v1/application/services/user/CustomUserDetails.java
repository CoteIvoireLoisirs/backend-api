package ca.deltagis.success.v1.application.services.user;

import ca.deltagis.success.v1.domain.core.entities.user.User;
import ca.deltagis.success.v1.domain.core.models.rigths.DefaultSystemRight;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    private List<GrantedAuthority> authorities;

    // Constructor accepting your custom domain User class
    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Given a role, this method returns a list of granted authorities for the user.
     *
     * @param role the role for which to get the authorities
     * @return a list of granted authorities
     */
    public List<GrantedAuthority> getAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(DefaultSystemRight.getByValue(role).toString()));
        authorities.add(new SimpleGrantedAuthority("READ_AUTHORITY"));
        authorities.add(new SimpleGrantedAuthority("WRITE_PRIVILEGE"));
        authorities.add(new SimpleGrantedAuthority("USER"));
        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // return user.getRole();
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // TODO : Change based on your requirements
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // TODO : Change based on your requirements
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // TODO : Change based on your requirements
    }

    @Override
    public boolean isEnabled() {
        return true;  // TODO : Change based on your requirements
    }

    /**
     * @return The user associated with this {@link UserDetails} instance.
     */
    public User getUser() {
        return user;
    }
}
