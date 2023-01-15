package com.promotion.handwriting.entity;

import com.promotion.handwriting.enums.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "ID", nullable = false)
    private String id;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType role;

    public User(String id, String password, UserType type) {
        this.id = id;
        this.password = password;
        this.role = type;
    }
}
