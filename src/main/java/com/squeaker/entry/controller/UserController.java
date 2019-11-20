package com.squeaker.entry.controller;

import com.squeaker.entry.body.UserChange;
import com.squeaker.entry.body.UserSignUp;
import com.squeaker.entry.body.getUserInfo.UserInfo;
import com.squeaker.entry.dao.UserDAO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping
    public void signUp(@RequestParam String userId, String userPw, String userName, String userIntro, String emailCode, @RequestParam("file") MultipartFile file) {
        new UserDAO().signUp(new UserSignUp(userId, userPw, userName, userIntro, emailCode), file);
    }

    @PutMapping
    public void changeUser(@RequestHeader("Authorization") String token, @RequestBody UserChange userChange) {
        new UserDAO().changeUser(token, userChange);
    }

    @DeleteMapping
    public void deleteUser(@RequestHeader("Authorization") String token) {
        new UserDAO().deleteUser(token);
    }

    @GetMapping("/{uuid}")
    public UserInfo getUser(@PathVariable int uuid) {
        return new UserDAO().getUser(uuid);
    }

}
