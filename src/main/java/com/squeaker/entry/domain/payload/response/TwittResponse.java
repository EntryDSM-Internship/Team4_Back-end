package com.squeaker.entry.domain.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class TwittResponse {
    private Integer twittId;
    private Integer twittUid;
    private String twittContent;
    private Long twittDate;
    private List<String> twittImage;
    private boolean isLike;
    private boolean deleteAble;

    @Builder
    public TwittResponse(Integer twittId, Integer twittUid, String twittContent, Long twittDate, List<String> twittImage, boolean isLike, boolean deleteAble) {
        this.twittId = twittId;
        this.twittUid = twittUid;
        this.twittContent = twittContent;
        this.twittDate = twittDate;
        this.twittImage = twittImage;
        this.isLike = isLike;
        this.deleteAble = deleteAble;
    }
}
