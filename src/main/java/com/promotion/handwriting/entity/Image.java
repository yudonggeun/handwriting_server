package com.promotion.handwriting.entity;

import javax.persistence.*;

@Entity
public class Image {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "ad_id")
    private long adId;
    @Column(name = "PRIORITY")
    private int priority;
    @Column(name = "IMAGE_NAME")
    private String imageName;

    protected Image() {
    }

    public Long getId() {
        return id;
    }

    public Image(long adId, int priority, String imageName) {
        this.adId = adId;
        this.priority = priority;
        this.imageName = imageName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getImageName() {
        return imageName;
    }
}
