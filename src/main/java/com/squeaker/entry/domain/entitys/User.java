package com.squeaker.entry.domain.entitys;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "int auto_increment")
    private Integer uuid;

    @Column(nullable = false, unique = true, length = 30)
    private String userId;

    @Column(nullable = false, length = 30)
    private String userPw;

    @Column(nullable = false, length = 12)
    private String userName;

    @Column(columnDefinition = "TEXT")
    private String userImage;

    @Column(length = 60)
    private String userIntro;

    @Column
    private String userRefreshToken;

    @Column(nullable = false, columnDefinition = "int(1) default 0")
    private Integer userPrivate;

    @Builder
    public User(String userId, String userPw, String userName, String userImage,
                String userIntro, String userRefreshToken, Integer userPrivate) {
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.userImage = userImage;
        this.userIntro = userIntro;
        this.userRefreshToken = userRefreshToken;
        this.userPrivate = userPrivate;
    }
}
