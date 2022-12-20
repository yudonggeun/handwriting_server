package com.promotion.handwriting.entity;

import com.promotion.handwriting.enums.AdType;

import javax.persistence.*;
import java.util.List;

@Entity
public class Ad {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "AD_TYPE")
    private AdType type;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "DETAIL")
    private String detail;
    @OneToMany(fetch = FetchType.LAZY)
    private List<Image> images;
}
