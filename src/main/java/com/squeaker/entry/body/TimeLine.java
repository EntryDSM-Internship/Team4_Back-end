package com.squeaker.entry.body;

import lombok.Getter;

import java.util.List;

@Getter
public class TimeLine {
    private int twittUid;
    private int twittUuid;
    private String twittContent;
    private String twittDate;
    private List twittImage;

    public TimeLine(int twittUid, int twittUuid, String twittContent, String twittDate, List twittImage) {
        this.twittUid = twittUid;
        this.twittUuid = twittUuid;
        this.twittContent = twittContent;
        this.twittDate = twittDate;
        this.twittImage = twittImage;
    }
}
