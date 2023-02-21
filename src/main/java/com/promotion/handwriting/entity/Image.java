package com.promotion.handwriting.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Image extends BasisEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private Ad ad;

    @Column(name = "PRIORITY")
    private int priority;
    @Column(name = "IMAGE_NAME")
    private String imageName;
    @Column(name = "COMPRESS_IMAGE_NAME")
    private String compressImageName;

    @Builder
    private Image(int priority, String imageName, String compressImageName) {
        this.priority = priority;
        this.imageName = imageName;
        this.compressImageName = compressImageName;
    }

    public int getPriority() {
        return priority;
    }

    public String getImageName() {
        return imageName;
    }

    public String getCompressImageName() {
        return compressImageName;
    }

    public void setAd(Ad ad) {
        updateModifyTime();
        this.ad = ad;
    }

    public void setImageName(String imageName) {
        updateModifyTime();
        this.imageName = imageName;
    }

    public void setCompressImageName(String compressImageName){
        this.compressImageName = compressImageName;
    }

    public void setPriority(int priority) {
        updateModifyTime();
        this.priority = priority;
    }
}
