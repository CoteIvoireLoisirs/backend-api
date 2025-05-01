package com.erastedev.ciexplore.v1.infrastructure.task.seeders;

import com.erastedev.ciexplore.v1.application.services.user.UserServiceImpl;
import com.erastedev.ciexplore.v1.domain.entities.user.model.User;
import com.erastedev.ciexplore.v1.domain.ports.out.seeders.AbstractCommonSeeder;
import com.erastedev.ciexplore.v1.domain.ports.out.seeders.SeederManager;
import com.erastedev.ciexplore.v1.infrastructure.repository.user.UserRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserSeeder extends AbstractCommonSeeder<User> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl service;

    @Autowired
    private SeederManager seederManager;

    Logger logger = LoggerFactory.getLogger(UserSeeder.class);

    @Override
    public void run() {
        logger.info("UserSeeder > Start ...");
        try {
            if (service.getAll().isEmpty()) {
                userRepository.saveAll(new ArrayList<>(fakeData()));
            }

            User checkUser = service.getUserByUsername("admin");
            logger.info("UserSeeder>checkUser: {}", checkUser);
            if (checkUser == null) {
                logger.info("UserSeeder>create admin user");
                userRepository.saveAll(new ArrayList<>(List.of(
                        buildVerifyeUser("admin", "admin@example.com", "admin")
                )));
            }
        } catch (Exception e) {
            logger.error("UserSeeder > Failed to seed: {}", e.getMessage());
            e.printStackTrace();
        }

        logger.info("UserSeeder > End ...");
    }

    @Override
    public boolean shouldRun() {
        return !seederManager.hasRun(this.getClass().getName());
    }

    @Override
    protected Faker faker() {
        return new Faker();
    }

    @Override
    protected int getLimit() {
        return 9;
    }

    @Override
    public List<User> fakeData() {
        try {
            List<User> users = new ArrayList<User>();
            for (int j = 0; j < getLimit(); j++) {
                users.add(buildVerifyeUser(faker().name().username(), faker().internet().emailAddress(), "password"));
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed to generate fake data: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Generates a fake user with the given username, email, and password.
     * The user is verified and has a random first and last name.
     *
     * @param username the username of the user
     * @param email    the email of the user
     * @param password the password of the user
     * @return the generated user
     */
    private User buildVerifyeUser(String username, String email, String password) {
        Date now = new Date();
        Timestamp nowTimestamp = new Timestamp(now.getTime());

        return User.builder()
                .username(username)
                .email(email)
                .password(service.encodePassword(password))
                .lastName(faker().name().lastName())
                .firstName(faker().name().firstName())
                .verifiedAt(now)
                .created(nowTimestamp)
                .updated(nowTimestamp)
                .build();
    }
}
