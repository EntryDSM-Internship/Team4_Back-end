package com.squeaker.entry.controller;

import com.squeaker.entry.domain.payload.request.UserSignUp;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user")
public class UserController {


    @PostMapping
    public void signUp(@RequestBody @NotNull UserSignUp userSignUp) {

    }

}
