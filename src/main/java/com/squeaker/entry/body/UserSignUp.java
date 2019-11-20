package com.squeaker.entry.body;

import lombok.Getter;

@Getter
public class UserSignUp {
    private String userId;
    private String userPw;
    private String userName;
    private String userIntro;
    private String emailCode;

    public UserSignUp(String userId, String userPw, String userName, String userIntro, String emailCode) {
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.userIntro = userIntro;
        this.emailCode = emailCode;
    }
}
