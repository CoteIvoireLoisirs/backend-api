/**
 * @Author: Eraste e.kouakou@omconsulting-group.com
 * @Date: 2024-10-20 00:14:18
 * @LastEditors: Eraste e.kouakou@omconsulting-group.com
 * @LastEditTime: 2024-12-06 19:38:13
 * @FilePath: src/main/java/ca/deltagis/success/v1/application/services/user/auth/CustomUserDetailsService.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package ca.deltagis.success.v1.application.services.user.auth;

import ca.deltagis.success.v1.application.services.user.UserServiceImpl;
import ca.deltagis.success.v1.infrastructure.repository.user.UserRepository;
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
        Optional<ca.deltagis.success.v1.domain.core.entities.user.User> userSearch = userRepository.findByUsername(username);

        List<String> roles = new ArrayList<>();
        roles.add("USER"); // TODO : get roles from user service

        logger.info("loadUserByUsername {}", userSearch);
        if (userSearch.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        ca.deltagis.success.v1.domain.core.entities.user.User user = userSearch.get();

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(roles.toArray(new String[0]))
                .build();
    }
}