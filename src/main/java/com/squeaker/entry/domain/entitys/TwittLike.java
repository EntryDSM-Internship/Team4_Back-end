package com.squeaker.entry.domain.entitys;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class TwittLike {

    @Id
    @Column
    private Integer twittId;

    private Integer uuid;

}
