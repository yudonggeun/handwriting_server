package com.promotion.handwriting.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

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

    @Builder
    private Image(int priority, String imageName) {
        this.priority = priority;
        this.imageName = imageName;
    }

    public int getPriority() {
        return priority;
    }

    public String getImageName() {
        return imageName;
    }

    public void setAd(Ad ad) {
        updateModifyTime();
        this.ad = ad;
    }

    public void setImageName(String imageName) {
        updateModifyTime();
        this.imageName = imageName;
    }

    public void setPriority(int priority) {
        updateModifyTime();
        this.priority = priority;
    }
}
