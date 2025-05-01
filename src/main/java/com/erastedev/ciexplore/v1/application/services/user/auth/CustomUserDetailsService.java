package com.erastedev.ciexplore.v1.application.services.user.auth;

import com.erastedev.ciexplore.v1.application.services.user.UserServiceImpl;
import com.erastedev.ciexplore.v1.infrastructure.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    /**
     * {@inheritDoc}
     *
     * @param username the username to search for
     * @return a {@link UserDetails} object containing the user's details
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.erastedev.ciexplore.v1.domain.entities.user.model.User> userSearch = userRepository.findByUsername(username)
                .or(() -> userRepository.findByEmail(username));

        if (userSearch.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        com.erastedev.ciexplore.v1.domain.entities.user.model.User user = userSearch.get();

        // Build the user details object
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(
                        // TODO: get roles from user service
                        new String[]{"USER"}
                )
                .build();
    }
}