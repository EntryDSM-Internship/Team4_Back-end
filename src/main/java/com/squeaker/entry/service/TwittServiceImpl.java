package com.squeaker.entry.service;

import com.squeaker.entry.domain.entitys.Follow;
import com.squeaker.entry.domain.entitys.Image;
import com.squeaker.entry.domain.entitys.Twitt;
import com.squeaker.entry.domain.entitys.User;
import com.squeaker.entry.domain.payload.response.TwittResponse;
import com.squeaker.entry.domain.repository.*;
import com.squeaker.entry.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TwittServiceImpl implements TwittService {

    private UserRepository userRepository;
    private TwittRepository twittRepository;
    private FollowRepository followRepository;
    private ImageRepository imageRepository;
    private TwittLikeRespository twittLikeRespository;

    public TwittServiceImpl(UserRepository userRepository, TwittRepository twittRepository, FollowRepository followRepository, ImageRepository imageRepository, TwittLikeRespository twittLikeRespository) {
        this.userRepository = userRepository;
        this.twittRepository = twittRepository;
        this.followRepository = followRepository;
        this.imageRepository = imageRepository;
        this.twittLikeRespository = twittLikeRespository;
    }

    @Override
    public List<TwittResponse> getTwitts(String token, Integer count) {
        User user = userRepository.findByUserId(JwtUtil.parseToken(token));
        List<Integer> relationUser = new ArrayList<>();

        relationUser.add(user.getUuid());
        for(Follow follow : followRepository.findByFollower(user.getUuid()))
            relationUser.add(follow.getFollowing());

        List<TwittResponse> twitts = new ArrayList<>();

        for(Twitt twitt : twittRepository.findByTwittUidInOrderByTwittDateDesc(relationUser)) {
            twitts.add(getTwittList(user, twitt, imageRepository, twittLikeRespository));
        }
        return twitts;
    }

    @Override
    public void addTwitt(String token, String content, MultipartFile[] files) {
        User user = userRepository.findByUserId(JwtUtil.parseToken(token));
        Integer uuid = user.getUuid();
        twittRepository.save(
                Twitt.builder()
                .twittUid(uuid)
                .twittContent(content)
                .twittDate(new Date().getTime())
                .build()
        );
    }

    static TwittResponse getTwittList(User user, Twitt twitt, ImageRepository imageRepository, TwittLikeRespository twittLikeRespository) {
        List<String> imageList;
        List<Image> images = imageRepository.findByTwittId(twitt.getTwittId());
        imageList = new ArrayList<>();
        boolean like;

        for(Image image : images)
            imageList.add(image.getImageName());

        like = twittLikeRespository.findByTwittIdAndAndUuid(twitt.getTwittId(), user.getUuid()) != null;

        return TwittResponse.builder()
                .twittId(twitt.getTwittId())
                .twittUid(twitt.getTwittUid())
                .twittContent(twitt.getTwittContent())
                .twittDate(twitt.getTwittDate())
                .twittImage(imageList)
                .isLike(like)
                .deleteAble(user.getUuid().equals(twitt.getTwittUid()))
                .build();
    }
}
