package com.promotion.handwriting.entity;

import com.promotion.handwriting.enums.UserType;
import javax.persistence.*;

@Entity
public class User {
    @Id
    @Column(name = "ID", nullable = false)
    private String id;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "USER_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type;
}
