package com.squeaker.entry.service;

import com.squeaker.entry.domain.entitys.*;
import com.squeaker.entry.domain.payload.response.TwittResponse;
import com.squeaker.entry.domain.repository.*;
import com.squeaker.entry.exception.TwittNotFoundException;
import com.squeaker.entry.exception.UserNotFoundException;
import com.squeaker.entry.exception.UserNotMatchException;
import com.squeaker.entry.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TwittServiceImpl implements TwittService {

    private UserRepository userRepository;
    private TwittRepository twittRepository;
    private FollowRepository followRepository;
    private ImageRepository imageRepository;
    private TwittLikeRespository twittLikeRespository;
    private CommentRepository commentRepository;

    public TwittServiceImpl(UserRepository userRepository, TwittRepository twittRepository, FollowRepository followRepository, ImageRepository imageRepository, TwittLikeRespository twittLikeRespository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.twittRepository = twittRepository;
        this.followRepository = followRepository;
        this.imageRepository = imageRepository;
        this.twittLikeRespository = twittLikeRespository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<TwittResponse> getTwitts(String token, Integer count) {
        User user = userRepository.findByUserId(JwtUtil.parseToken(token));
        if(user == null) throw new UserNotFoundException();
        List<Integer> relationUser = new ArrayList<>();

        relationUser.add(user.getUuid());
        for(Follow follow : followRepository.findByFollower(user.getUuid()))
            relationUser.add(follow.getFollowing());

        List<TwittResponse> twitts = new ArrayList<>();
        List<Twitt> twittList = twittRepository.findByTwittUidInOrderByTwittDateDesc(relationUser);

        for(int i = (count-1)*10; i < (count*10)-1; i++) {
            try {
                twitts.add(getTwittInfo(user, twittList.get(i), imageRepository, twittLikeRespository, commentRepository));
            } catch (Exception e){
                break;
            }
        }
        return twitts;
    }

    @Override
    public TwittResponse getTwitt(String token, Integer twittId) {
        User user = userRepository.findByUserId(JwtUtil.parseToken(token));
        Twitt twitt = twittRepository.findByTwittId(twittId);
        if(twitt == null) throw new TwittNotFoundException();

        return getTwittInfo(user, twitt, imageRepository, twittLikeRespository, commentRepository);
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

    @Override
    public void deleteTwit(String token, Integer twittId) {
        User user = userRepository.findByUserId(JwtUtil.parseToken(token));
        Twitt twitt = twittRepository.findByTwittId(twittId);
        if(user == null) throw new UserNotFoundException();
        if(twitt == null) throw new TwittNotFoundException();
        if(!user.getUuid().equals(twitt.getTwittUid())) throw new UserNotMatchException();

        TwittLike twittLike = twittLikeRespository.findByTwittIdAndAndUuid(twitt.getTwittId(), user.getUuid());
        List<Image> images = imageRepository.findByTwittId(twittId);

        if(twittLike != null) twittLikeRespository.delete(twittLike);
        for (Image i : images) imageRepository.delete(i);
        twittRepository.delete(twitt);
    }

    static TwittResponse getTwittInfo(User user, Twitt twitt, ImageRepository imageRepository, TwittLikeRespository twittLikeRespository, CommentRepository commentRepository) {
        List<String> imageList = new ArrayList<>();
        List<Image> images = imageRepository.findByTwittId(twitt.getTwittId());
        List<Comment> comments = commentRepository.findByCommentTwitIdOrderByCommentDateAsc(twitt.getTwittId());

        for(Image image : images)
            imageList.add(image.getImageName());

        return TwittResponse.builder()
                .twittId(twitt.getTwittId())
                .twittUid(twitt.getTwittUid())
                .twittContent(twitt.getTwittContent())
                .twittDate(twitt.getTwittDate())
                .twittImage(imageList)
                .comments(comments)
                .isLike(twittLikeRespository.findByTwittIdAndAndUuid(twitt.getTwittId(), user.getUuid()) != null)
                .deleteAble(user.getUuid().equals(twitt.getTwittUid()))
                .build();
    }
}
