/*
 * Used by UserController to speak with UserRepository
 */
package com.example.fileuploaderapp.services;

import com.example.fileuploaderapp.models.User;
import com.example.fileuploaderapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates and saves a user in the database. UserService object passwordEncoder makes sure password is
     * encrypted.
     * @param username name of user
     * @param password password of user.
     * @return the newly created and saved user object.
     */
    public User registerUser(String username, String password){
        var user = new User(UUID.randomUUID().toString(),
                username,
                passwordEncoder.encode(password)
        );

        return userRepository.save(user);
    }

    /**
     * Used widely to make sure files related to one user is not handled by another user.
     * Something to note is that user login has been authenticated at this point.
     * @param username name of user to load.
     * @return the User object
     * @throws UsernameNotFoundException could not find a user with that name.
     */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("A user with username '" + username + "' could not be found."));

        return user;
    }
}
