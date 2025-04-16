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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<com.erastedev.ciexplore.v1.domain.entities.user.model.User> userSearch = userRepository.findByUsername(username);

            List<String> roles = new ArrayList<>();
            // TODO : get roles from user service
            roles.add("USER");

            logger.info("loadUserByUsername {}", userSearch);
            if (userSearch.isEmpty()) {
                throw new UsernameNotFoundException("User not found");
            }

            com.erastedev.ciexplore.v1.domain.entities.user.model.User user = userSearch.get();

            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(roles.toArray(new String[0]))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("User not found");
        }
    }
}