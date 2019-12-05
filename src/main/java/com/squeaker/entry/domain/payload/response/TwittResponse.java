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
    private String twittDate;
    private List<String> twittImage;
    private boolean deleteAble;

    @Builder
    public TwittResponse(Integer twittId, Integer twittUid, String twittContent, String twittDate, List<String> twittImage, boolean deleteAble) {
        this.twittId = twittId;
        this.twittUid = twittUid;
        this.twittContent = twittContent;
        this.twittDate = twittDate;
        this.twittImage = twittImage;
        this.deleteAble = deleteAble;
    }
}
