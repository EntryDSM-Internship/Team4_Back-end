package com.squeaker.entry.body.getUserInfo;

import lombok.Getter;

@Getter
public class UserFollowers {
    private int uuid;
    private String userId;
    private String userName;
    private String userImage;

    public UserFollowers(int uuid, String userId, String userName, String userImage) {
        this.uuid = uuid;
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
    }
}
