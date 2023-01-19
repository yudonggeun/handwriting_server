package com.promotion.handwriting.entity;

import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.util.FileUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Slf4j
@NoArgsConstructor
public class Ad extends BasisEntity{
    @Enumerated(EnumType.STRING)
    @Column(name = "AD_TYPE")
    private AdType type;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "DETAIL")
    private String detail;
    @Column(name = "RESOURCE_PATH")
    private String resourcePath;
    @OneToMany(mappedBy = "ad", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    public static Ad createAd(AdType type, String title, String detail, String resourcePath) throws IOException {
        FileUtil.createImageDirectory(resourcePath);
        return new Ad(type, title, detail, resourcePath);
    }

    private Ad(AdType type, String title, String detail, String resourcePath) {
        super();
        this.type = type;
        this.title = title;
        this.detail = detail;
        this.resourcePath = resourcePath;
        this.images = new ArrayList<>();
    }

    public void setTitle(String title) {
        updateModifyTime();
        this.title = title;
    }

    public void setDetail(String detail) {
        updateModifyTime();
        this.detail = detail;
    }

    public void addImage(Image image){
        if(this.images.stream().filter(i -> i.getImageName().equals(image.getImageName())).toArray().length == 0) {
            image.setAd(this);
            this.images.add(image);
        } else {
            log.error("Ad.addImage : 저장할 이미지의 이름이 이미 존재합니다. [저장실패이미지]" + image.getImageName());
        }
    }

    public void addImages(List<Image> images){
        Set<String> imageNames = this.images.stream().map(Image::getImageName).collect(Collectors.toSet());
        for (Image image : images) {
            if(imageNames.contains(image.getImageName())){
                log.error("Ad.addImage : 저장할 이미지의 이름이 이미 존재합니다. [저장실패이미지]" + image.getImageName());
            } else {
                image.setAd(this);
                this.images.add(image);
            }
        }
    }
}
