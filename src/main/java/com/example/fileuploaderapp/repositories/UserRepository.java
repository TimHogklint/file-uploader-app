/*
 * User repository : spring-boot interface connection to database.
 * Creates easy way to query content.
 */
package com.example.fileuploaderapp.repositories;

import com.example.fileuploaderapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    /**
     * Find a user by name.
     * @param username name to look for.
     * @return returns a user with this name.
     */
    Optional<User> findByUsername(String username);
}
