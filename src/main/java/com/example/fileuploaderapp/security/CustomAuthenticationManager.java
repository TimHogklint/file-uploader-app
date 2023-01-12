/*
 * Used inside JWTLoginFilter to authenticate user.
 */

package com.example.fileuploaderapp.security;

import com.example.fileuploaderapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {

    private final PasswordEncoder encoder;
    private final UserService userService;

    @Autowired
    public CustomAuthenticationManager(PasswordEncoder encoder, UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    /**
     * Takes in a users authentication details and allows/denies "token" in the filter chain.
     * @param authentication the encrypted data to verify
     * @return valid login token
     * @throws AuthenticationException invalid login or bad/type exception.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final UserDetails userDetail = userService.loadUserByUsername(authentication.getName());
        if (!encoder.matches(authentication.getCredentials().toString(), userDetail.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        return new UsernamePasswordAuthenticationToken(userDetail.getUsername(), userDetail.getPassword(), userDetail.getAuthorities());
    }
}