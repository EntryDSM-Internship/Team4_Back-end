package com.squeaker.entry.domain.entitys;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Twitt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer twitId;

    @Column
    private Integer twittUid;

    @Column
    private String twittDate;

    @Column
    private String twittContent;

    @Builder
    public Twitt(Integer twittUud, String twittDate, String twittContent) {
        this.twittUid = twittUid;
        this.twittDate = twittDate;
        this.twittContent = twittContent;
    }
}
