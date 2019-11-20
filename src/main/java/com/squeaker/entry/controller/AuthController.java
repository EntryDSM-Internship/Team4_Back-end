package com.squeaker.entry.controller;

import com.squeaker.entry.body.Token;
import com.squeaker.entry.body.UserSignIn;
import com.squeaker.entry.dao.AuthDAO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping
    public Token signIn(@RequestBody UserSignIn userSignIn) {
        return new AuthDAO().signIn(userSignIn);
    }

    @PutMapping
    public Token refreshToken(@RequestHeader("Authorization") String accessToken) {
        return new AuthDAO().refreshToken(accessToken);
    }

}
