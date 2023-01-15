package com.promotion.handwriting.entity;

import com.promotion.handwriting.enums.UserType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class User extends BasisEntity {
    @Column(name = "USER_ID", nullable = false)
    private String userId;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType role;

    @Builder
    private User(String userId, String password, UserType type) {
        this.userId = userId;
        this.password = password;
        this.role = type;
    }
}
