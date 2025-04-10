package com.erastedev.ciexplore.v1.application.mock;

import com.erastedev.ciexplore.v1.domain.entities.user.User;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Getter
public class UserMock {
    private static Logger logger = LoggerFactory.getLogger(UserMock.class);

    /**
     * Creates a mock user.
     *
     * @return a mock user
     */
    public static User user() {
        User user = new User();
        user.setId(1000L);
        user.setUuid(UUID.randomUUID());
        user.setUsername("test");
        user.setEmail("test@gmail.com");
        user.setPassword("mypassword");
        logger.info("UserMock>user: {}", user);
        return user;
    }

    public static User notRegisteredUser() {
        User user = new User();
        user.setId(1000L);
        user.setUsername("xxxxx");
        user.setEmail("xxxxx@example.com");
        user.setPassword("xxxxxxxx");
        return user;
    }

    public static String tokenNotConnected() {
        return "xxx.a.eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRlc3RAZ21haWwuY29tIiwic3ViIjoidGVzdEBnbWFpbC5jb20iLCJpYXQiOjE3MzQ2NzQ2OTUsImV4cCI6MTczNDg0NzQ5NX0.C6ZY_DEBvPofNkygRzsOWq7cJmF7slfyJYovnH5l-t4";
    }

    public static String token() {
        return "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRlc3RAZ21haWwuY29tIiwic3ViIjoidGVzdEBnbWFpbC5jb20iLCJpYXQiOjE3MzQ2NzQ2OTUsImV4cCI6MTczNDg0NzQ5NX0.C6ZY_DEBvPofNkygRzsOWq7cJmF7slfyJYovnH5l-t4";
    }
}
