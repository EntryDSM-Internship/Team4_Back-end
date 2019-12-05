package com.squeaker.entry.service;

import com.squeaker.entry.domain.entitys.Follow;
import com.squeaker.entry.domain.entitys.Image;
import com.squeaker.entry.domain.entitys.Twitt;
import com.squeaker.entry.domain.entitys.User;
import com.squeaker.entry.domain.payload.response.TwittResponse;
import com.squeaker.entry.domain.repository.FollowRepository;
import com.squeaker.entry.domain.repository.ImageRepository;
import com.squeaker.entry.domain.repository.TwittRepository;
import com.squeaker.entry.domain.repository.UserRepository;
import com.squeaker.entry.utils.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TwittServiceImpl implements TwittService {

    private UserRepository userRepository;
    private TwittRepository twittRepository;
    private FollowRepository followRepository;
    private ImageRepository imageRepository;

    public TwittServiceImpl(UserRepository userRepository, TwittRepository twittRepository, FollowRepository followRepository, ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.twittRepository = twittRepository;
        this.followRepository = followRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public List<TwittResponse> getTwitts(String token, Integer count) {
        User user = userRepository.findByUserId(JwtUtil.parseToken(token));
        List<Integer> relationUser = new ArrayList<>();

        relationUser.add(user.getUuid());
        for(Follow follow : followRepository.findByFollower(user.getUuid())) {
            relationUser.add(follow.getFollowing());
        }

        List<TwittResponse> twitts = new ArrayList<>();
        List<String> imageList;
        for(Twitt twitt : twittRepository.findByTwittUidInOrderByTwittDateDesc(relationUser)) {
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
                    .deleteAble(user.getUuid().equals(twitt.getTwittUid()))
                    .build()
            );
        }
        return twitts;
    }
}
