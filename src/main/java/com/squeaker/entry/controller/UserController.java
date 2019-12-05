package com.squeaker.entry.controller;

import com.squeaker.entry.domain.entitys.User;
import com.squeaker.entry.domain.payload.request.UserSignUp;
import com.squeaker.entry.domain.payload.response.AuthCodeResponse;
import com.squeaker.entry.domain.payload.response.user.UserResponse;
import com.squeaker.entry.service.UserServiceImpl;
import com.squeaker.entry.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @PostMapping("/authemail")
    public AuthCodeResponse authEmail(@RequestParam @NotNull String email) {
        return userService.authEmail(email);
    }

    @GetMapping("/{uuid}")
    public UserResponse getUser(@PathVariable Integer uuid) {
        return userService.getUser(uuid);
    }

    @PostMapping
    public void signUp(@RequestParam String userId, String userPw, String userName, String userIntro,
                       String emailCode, @RequestParam(value = "file", required = false) MultipartFile file) {
        userService.signUp(
                UserSignUp.builder()
                .userId(userId)
                .userPw(userPw)
                .userName(userName)
                .userIntro(userIntro)
                .emailcode(emailCode)
                .multipartFile(file)
                .build()
        );
    }

    @PutMapping
    public void changeUser(@RequestHeader("Authotization") String token,
                           @RequestParam(required = false) String userPw, String userName, String userIntro,
                           Integer userPrivate, @RequestParam(value = "file", required = false) MultipartFile file) {

        userService.changeUser(
                User.builder()
                .userId(JwtUtil.parseToken(token))
                .userPw(userPw)
                .userName(userName)
                .userIntro(userIntro)
                .userPrivate(userPrivate)
                .build(),
                file
        );
    }

    @DeleteMapping
    public void deleteUser(@RequestHeader("Authorization") String token) {
        userService.deleteUser(token);
    }

}
