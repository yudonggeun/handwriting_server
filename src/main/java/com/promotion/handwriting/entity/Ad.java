package com.promotion.handwriting.entity;

import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.util.FileUtil;
import lombok.Getter;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

@Entity
@Getter
public class Ad {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "AD_TYPE")
    private AdType type;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "DETAIL")
    private String detail;
    @Column(name = "RESOURCE_PATH")
    private String resourcePath;
    @OneToMany(mappedBy = "ad", fetch = FetchType.LAZY)
    private List<Image> images;

    protected Ad() {
    }

    public static Ad getProxyAd(long id){
        return new Ad(id);
    }

    private Ad(long id){
        this.id = id;
    }

    public Ad(AdType type, String title, String detail, String resourcePath, List<Image> images) throws IOException {
        this.type = type;
        this.title = title;
        this.detail = detail;
        this.resourcePath = resourcePath;
        this.images = images;

        FileUtil.createImageDirectory(resourcePath);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
