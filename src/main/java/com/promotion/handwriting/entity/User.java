package com.promotion.handwriting.entity;

import com.promotion.handwriting.enums.UserType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "USERS")
public class User extends BasisEntity {
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType role;

    @Builder
    private User(String userId, String password, UserType type) {
        this.userId = userId;
        this.password = password;
        this.role = type;
    }
}
