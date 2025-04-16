package com.erastedev.ciexplore.v1.infrastructure.repository.user;

import com.erastedev.ciexplore.v1.domain.entities.user.model.User;
import com.erastedev.ciexplore.v1.domain.ports.in.ICommonRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends ICommonRepository<User> {
    /**
     * Retrieves all users that are not marked as deleted.
     *
     * @return a list of users that are not marked as deleted
     */
    List<User> findAllByDeletedIsNull();

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the user details if found, or an empty Optional if no such user exists
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user to find
     * @return an Optional containing the user details if found, or an empty Optional if no such user exists
     */
    Optional<User> findByEmail(String email);
}

