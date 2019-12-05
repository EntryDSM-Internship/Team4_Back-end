package com.squeaker.entry.domain.entitys;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
public class EmailAuth {

    @Id
    @Column(length = 30)
    private String authEmail;

    @Column(nullable = false, length = 36)
    private String authCode;

    @Builder
    public EmailAuth(String authEmail, String authCode) {
        this.authEmail = authEmail;
        this.authCode = authCode;
    }
}
