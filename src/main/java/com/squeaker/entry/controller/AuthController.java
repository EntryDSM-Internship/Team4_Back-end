package com.squeaker.entry.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
public class AuthController {

    @PostMapping
    public Token signin() {

    }

}
