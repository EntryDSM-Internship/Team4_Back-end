package com.squeaker.entry.body.getUserInfo;

import com.squeaker.entry.body.TimeLine;
import lombok.Getter;

import java.util.List;

@Getter
public class UserInfo {
    private int uuid;
    private String userId;
    private String userName;
    private String userIntro;
    private String userImage;
    private List<TimeLine> timeline;
    private List<UserFollowers> follower;
    private List<UserFollowers> following;

    public UserInfo(
            int uuid,
            String userId,
            String userName,
            String userIntro,
            String userImage,
            List<TimeLine> timeLine,
            List<UserFollowers> follower,
            List<UserFollowers> following) {
        this.uuid = uuid;
        this.userId = userId;
        this.userName = userName;
        this.userIntro = userIntro;
        this.userImage = userImage;
        this.timeline = timeLine;
        this.follower = follower;
        this.following = following;
    }
}
