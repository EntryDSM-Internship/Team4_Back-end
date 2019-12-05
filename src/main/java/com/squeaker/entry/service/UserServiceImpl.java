package com.squeaker.entry.service;

import com.squeaker.entry.domain.entitys.*;
import com.squeaker.entry.domain.payload.request.UserSignUp;
import com.squeaker.entry.domain.payload.response.AuthCodeResponse;
import com.squeaker.entry.domain.payload.response.TwittResponse;
import com.squeaker.entry.domain.payload.response.user.FollowResponse;
import com.squeaker.entry.domain.payload.response.user.UserResponse;
import com.squeaker.entry.domain.repository.*;
import com.squeaker.entry.exception.InvalidAuthEmailException;
import com.squeaker.entry.exception.InvalidFileException;
import com.squeaker.entry.exception.UserAlreadyExistsException;
import com.squeaker.entry.exception.UserNotFoundException;
import com.squeaker.entry.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private AuthMailRepository authMailRepository;
    private UserRepository userRepository;
    private FollowRepository followRepository;
    private TwittRepository twittRepository;
    private ImageRepository imageRepository;

    public UserServiceImpl(AuthMailRepository authMailRepository, UserRepository userRepository, FollowRepository followRepository, TwittRepository twittRepository, ImageRepository imageRepository) {
        this.authMailRepository = authMailRepository;
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.twittRepository = twittRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public AuthCodeResponse authEmail(String email) {
        String uuid = UUID.randomUUID().toString();
        authMailRepository.save(
                EmailAuth.builder()
                        .authEmail(email)
                        .authCode(uuid)
                        .build()
        );

        return new AuthCodeResponse(uuid);
    }

    @Override
    public UserResponse getUser(Integer uuid) {
        User user = userRepository.findByUuid(uuid);

        List<TwittResponse> twitts = new ArrayList<>();
        List<FollowResponse> follower = new ArrayList<>();
        List<FollowResponse> following = new ArrayList<>();
        List<String> imageList;

        for(Twitt twitt : twittRepository.findByTwittUid(user.getUuid())) {
            List<Image> images = imageRepository.findByTwittId(twitt.getTwitId());
            imageList = new ArrayList<>();
            for(Image image : images)
                imageList.add(image.getImageName());

            twitts.add(
                    TwittResponse.builder()
                    .twittId(twitt.getTwitId())
                    .twittUid(twitt.getTwittUid())
                    .twittContent(twitt.getTwittContent())
                    .twittDate(twitt.getTwittDate())
                    .twittImage(imageList)
                    .build()
            );
        }
        for(Follow follows : followRepository.findByFollowerOrFollowing(user.getUuid(), user.getUuid())) {
            if(follows.getFollower().equals(user.getUuid())) {
                User follow = userRepository.findByUuid(follows.getFollowing());
                following.add(
                        FollowResponse.builder()
                        .uuid(follow.getUuid())
                        .userId(follow.getUserId())
                        .userName(follow.getUserName())
                        .userImage(follow.getUserId()+".jpg")
                        .build()
                );
            } else {
                User follow = userRepository.findByUuid(follows.getFollower());
                follower.add(
                        FollowResponse.builder()
                                .uuid(follow.getUuid())
                                .userId(follow.getUserId())
                                .userName(follow.getUserName())
                                .userImage(follow.getUserId()+".jpg")
                                .build()
                );
            }
        }

        return UserResponse.builder()
                .uuid(user.getUuid())
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userIntro(user.getUserIntro())
                .userImage(user.getUserId()+".jpg")
                .timeLine(twitts)
                .follower(follower)
                .following(following)
                .build();
    }

    @Override
    public void signUp(UserSignUp userSignUp) {

        if(userRepository.existsByUserId(userSignUp.getUserId())) throw new UserAlreadyExistsException();
        EmailAuth auth = authMailRepository.findByAuthEmailAndAuthCode(userSignUp.getUserId(), userSignUp.getEmailcode());
        if(auth == null) throw new InvalidAuthEmailException();
        if(userSignUp.getMultipartFile() != null) {
            File file = new File("D:\\Squeaker\\user\\"+userSignUp.getUserId()+".jpg");
            try {
                userSignUp.getMultipartFile().transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
                throw new InvalidFileException();
            }
        }

        authMailRepository.delete(authMailRepository.findByAuthEmail(userSignUp.getUserId()));
        userRepository.save(
                User.builder()
                .userId(userSignUp.getUserId())
                .userPw(userSignUp.getUserPw())
                .userName(userSignUp.getUserName())
                .userIntro(userSignUp.getUserIntro())
                .userPrivate(0)
                .build()
        );
    }

    @Override
    public void changeUser(User info, MultipartFile file) {
        User user = userRepository.findByUserId(info.getUserId());
        if(user == null) throw new UserNotFoundException();

        if (info.getUserPw() != null) user.setUserPw(info.getUserPw());
        if (info.getUserName() != null) user.setUserName(info.getUserName());
        if (info.getUserIntro() != null) user.setUserIntro(info.getUserIntro());
        if (info.getUserPrivate() != null) user.setUserPrivate(info.getUserPrivate());

        userRepository.save(user);
    }

    @Override
    public void deleteUser(String token) {
        String userId = JwtUtil.parseToken(token);
        User user = userRepository.findByUserId(userId);
        if(user == null) throw new UserNotFoundException();

        userRepository.delete(user);
    }
}
