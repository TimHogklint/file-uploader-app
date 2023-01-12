/*
 * This works in conjunction with JWTFilterLogin as requests are made.
 */

package com.example.fileuploaderapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.fileuploaderapp.services.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JWTVerifyFilter extends OncePerRequestFilter {

    private final UserService userService;

    public JWTVerifyFilter(UserService userService) {
        this.userService = userService;
    }


    /**
     *  Process incoming requests.
     * @param request the incoming request.
     * @param response the processed jwt response
     * @param filterChain the jwt process member.
     * @throws ServletException returns if connection could not resolve.
     * @throws IOException the authprocess could not complete or failed.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException
    {
        var authorizationHeader = request.getHeader("Authorization");

        // Make sure the header is not null or not formatted correctly.
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }

        var jwtToken = authorizationHeader.substring("Bearer ".length());

        // if token is empty there is nothing we can do, return.
        if(jwtToken.isEmpty()){
            filterChain.doFilter(request,response);
            return;
        }

        // Attempt to decrypt access token.
        try {
            var algorithm = Algorithm.HMAC256("secret");
            var verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();

            DecodedJWT jwt = verifier.verify(jwtToken);

            var user = userService.loadUserByUsername(jwt.getSubject());

            var authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    user.getPassword(),
                    user.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // reaching this point we can allow the request to filter down the chain.
            filterChain.doFilter(request, response);

        } catch (JWTVerificationException exception){
            throw new IllegalStateException("Failed to authenticate");
        }
    }
}
