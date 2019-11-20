package com.squeaker.entry.dao;

import com.squeaker.entry.body.TimeLine;
import com.squeaker.entry.body.UserChange;
import com.squeaker.entry.body.UserSignUp;
import com.squeaker.entry.body.getUserInfo.UserFollowers;
import com.squeaker.entry.body.getUserInfo.UserInfo;
import com.squeaker.entry.exception.InvalidAuthEmailException;
import com.squeaker.entry.exception.InvalidBodyException;
import com.squeaker.entry.exception.UserAlreadyExistsException;
import com.squeaker.entry.exception.UserNotFoundException;
import com.squeaker.entry.utils.JwtUtil;
import com.squeaker.entry.utils.Query;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends Query {

    public void signUp(UserSignUp userSignUp, MultipartFile file) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query(
                    "select * from user where userId='%s'",
                    userSignUp.getUserId()
            ));
            if(resultSet.next()) {
                throw new UserAlreadyExistsException();
            }

            resultSet = statement.executeQuery(query(
                    "select * from emailAuth where auth_email='%s' and auth_code='%s'",
                    userSignUp.getUserId(),
                    userSignUp.getEmailCode()
            ));
            if (!resultSet.next()) {
                throw new InvalidAuthEmailException();
            }

            statement.executeUpdate(query(
                    "insert into user values (default, '%s', '%s', '%s', '', '%s', '', 0)",
                    userSignUp.getUserId(),
                    userSignUp.getUserPw(),
                    userSignUp.getUserName(),
                    userSignUp.getUserIntro()
            ));
            resultSet = statement.executeQuery(query("select * from user where userId='%s'", userSignUp.getUserId()));
            int uuid = resultSet.getInt("uuid");
            if (!file.isEmpty()) {
                byte[] data = file.getBytes();
                FileOutputStream fileOutputStream = new FileOutputStream("/static/user/"+uuid+".jpg");
                fileOutputStream.write(data);
                fileOutputStream.close();
            }
            return;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        throw new InvalidBodyException();
    }

    public void changeUser(String token, UserChange userChange) {
        try {
            String uuid = JwtUtil.parseToken(token);
            statement = connection.createStatement();
            statement.executeUpdate(query(
                    "update user set userPw='%s', userName='%s', userIntro='%s', userPrivate=%s where uuid=%s",
                    userChange.getUserPw(),
                    userChange.getUserName(),
                    userChange.getUserIntro(),
                    userChange.getUserPrivate(),
                    uuid
            ));
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new InvalidBodyException();
    }

    public void deleteUser(String token) {
        try {
            String uuid = JwtUtil.parseToken(token);
            statement = connection.createStatement();
            statement.executeUpdate(query(
                    "delete from user where uuid=%s",
                    uuid
            ));
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new InvalidBodyException();
    }

    public UserInfo getUser(int uuid) {
        try {
            String userId;
            String userName;
            String userIntro;
            String userImage;
            List<TimeLine> timeLine = new ArrayList<>();
            List<UserFollowers> follower = new ArrayList<>();
            List<UserFollowers> following = new ArrayList<>();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query(
                    "select * from user where uuid=%s",
                    uuid
            ));
            if(resultSet.next()) {
                // 유저 정보
                userId = resultSet.getString("userId");
                userName = resultSet.getString("userName");
                userIntro = resultSet.getString("userIntro");
                userImage = resultSet.getString("userImage");
                UserFollowers userFollowers;

                // 유저 타입라인
                resultSet = statement.executeQuery(query(
                        "select * from twitt where twitt_uuid=%s",
                        uuid
                ));
                while(resultSet.next()) {
                    int twittUid = resultSet.getInt("twitt_id");
                    int twittUuid = resultSet.getInt("twitt_uuid");
                    String twittContent = resultSet.getString("twitt_content");
                    String twittDate = resultSet.getString("twitt_date");
                    List<String> images = new ArrayList<>();
                    // 해당 트윗의 사진들
                    resultSet = statement.executeQuery(query(
                            "select * from image where imageBoard=%s",
                            twittUid
                    ));
                    while (resultSet.next()) {
                        String image = resultSet.getString("imageName");
                        images.add(image);
                    }
                    TimeLine twitt = new TimeLine(twittUid, twittUuid, twittContent, twittDate, images);
                    timeLine.add(twitt);
                }

                // 유저 팔로워
                resultSet = statement.executeQuery(query(
                        "select * from follower where following=%s",
                        uuid
                ));
                while (resultSet.next()) {
                    int followerUuid = resultSet.getInt("follower");
                    String followerId = null;
                    String followerName = null;
                    String followerImage = null;
                    resultSet = statement.executeQuery(query(
                            "select * from user where uuid=%s",
                            followerUuid
                    ));
                    if(resultSet.next()) {
                        followerId = resultSet.getString("userId");
                        followerName = resultSet.getString("userName");
                        followerImage = resultSet.getString("userImage");
                    }
                    follower.add(new UserFollowers(followerUuid, followerId, followerName, followerImage));
                }

                // 유저 팔로잉
                resultSet = statement.executeQuery(query(
                        "select * from follower where follower=%s",
                        uuid
                ));
                while (resultSet.next()) {
                    int followingUuid = resultSet.getInt("following");
                    String followingId = null;
                    String followingName = null;
                    String followingImage = null;
                    resultSet = statement.executeQuery(query(
                            "select * from user where uuid=%s",
                            followingUuid
                    ));
                    if(resultSet.next()) {
                        followingId = resultSet.getString("userId");
                        followingName = resultSet.getString("userName");
                        followingImage = resultSet.getString("userImage");
                    }
                    userFollowers = new UserFollowers(followingUuid, followingId, followingName, followingImage);

                    following.add(userFollowers);
                }

                return new UserInfo(uuid, userId, userName, userIntro, userImage, timeLine, follower, following);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new UserNotFoundException();
    }

}
