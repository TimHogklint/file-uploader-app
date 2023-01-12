package com.example.fileuploaderapp.controllers;

import com.example.fileuploaderapp.services.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }


    /**
     * An all access route for users to create a user login.
     * @param registerUser basic input class object containing username and password.
     * @return will return the username as response.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(
        @RequestBody RegisterUser registerUser
    ){
        var user = userService.registerUser(registerUser.getUsername(), registerUser.getPassword());
        return ResponseEntity.ok(user.getUsername());
    }

    // Used registration process.
    @Getter
    @Setter
    public static class RegisterUser{
      private String username;
      private String password;
    }

    // Used during login process.
    @Getter
    @Setter
    public static class LoginUser{
        private String username;
        private String password;
    }
}
