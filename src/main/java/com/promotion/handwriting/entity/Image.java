package com.promotion.handwriting.entity;

import com.promotion.handwriting.dto.ImageUrlDto;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString
@NoArgsConstructor
public class Image extends BasisEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTENTS")
    private Ad ad;
    @Column(name = "PRIORITY")
    private int priority;
    @Column(name = "IMAGE_NAME", unique = true, nullable = false)
    private String imageName;
    @Column(name = "COMPRESS_IMAGE_NAME", unique = true)
    private String compressImageName;

    @Builder
    private Image(Ad content, int priority, String imageName, String compressImageName) {
        this.ad = content;
        this.priority = priority;
        this.imageName = imageName;
        this.compressImageName = compressImageName == null ? imageName : compressImageName;
    }

    public String getImageName() {
        return imageName;
    }

    public String getCompressImageName() {
        return compressImageName;
    }

    public String getImageUrl() {
        return "/" + imageName;
    }

    public String getCompressImageUrl() {
        return "/" + compressImageName;
    }


    public ImageUrlDto urlDto() {
        return new ImageUrlDto(getId(), getImageUrl(), getCompressImageUrl());
    }
}
