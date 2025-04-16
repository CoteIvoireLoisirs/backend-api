package com.erastedev.ciexplore.v1.infrastructure.task.seeders;

import com.erastedev.ciexplore.v1.application.services.user.UserServiceImpl;
import com.erastedev.ciexplore.v1.domain.entities.user.model.User;
import com.erastedev.ciexplore.v1.domain.ports.out.seeders.AbstractCommonSeeder;
import com.erastedev.ciexplore.v1.domain.ports.out.seeders.SeederManager;
import com.erastedev.ciexplore.v1.infrastructure.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserSeeder extends AbstractCommonSeeder {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl service;

    @Autowired
    private SeederManager seederManager;

    Logger logger = LoggerFactory.getLogger(UserSeeder.class);

    @Override
    public void run() {
        User checkUser = service.getUserByUsername("admin");
        logger.info("UserSeeder>checkUser: {}", checkUser);
        if (checkUser == null) {
            logger.info("UserSeeder>create admin user");
            User admin = User.builder()
                    .username("admin")
                    .email("admin@omconsulting-group.com")
                    .password(service.encodePassword("admin"))
                    .lastName("Admin")
                    .build();

            userRepository.saveAll(new ArrayList<>(List.of(admin)));
        }
    }

    @Override
    public boolean shouldRun() {
        return !seederManager.hasRun(this.getClass().getName());
    }
}
