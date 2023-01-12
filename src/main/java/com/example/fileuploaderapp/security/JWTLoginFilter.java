/*
 * This verifies the user login during the jwt filter chain - runs ahead of requests.
 */

package com.example.fileuploaderapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;

    /**
     * Formats the headers - preps the user info for verification phase.
     * @param request contains username , password.
     * @param response unused.
     * @return verified access token
     * @throws AuthenticationException invalid format / wrong username , password.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request , HttpServletResponse response)
            throws AuthenticationException
    {
        var username = request.getHeader("username");
        var password = request.getHeader("password");

        var authentication = new UsernamePasswordAuthenticationToken(username,password);

        return  authenticationManager.authenticate(authentication);
    }

    /**
     * On a valid authorization send back response with token.
     * @param request unused
     * @param response the response header containing a valid token for access.
     * @param filterChain jwt process member , part of intercepting requests.
     * @param authResult token subject field result.
     * @throws ServletException returns if connection cant resolve.
     * @throws IOException authprocess failed.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain,
                                    Authentication authResult)
            throws ServletException, IOException
    {

        try{
            var algorithm = Algorithm.HMAC256("secret");
            var token = JWT.create()
                    .withIssuer("auth0")
                    .withSubject(authResult.getName())
                    .sign(algorithm);

            response.addHeader("Access-Control-Expose-Headers","Authorization");
            response.addHeader("Authorization","Bearer " + token);
        }
        catch (JWTCreationException exception)
        {
            exception.printStackTrace();
        }
    }
}
