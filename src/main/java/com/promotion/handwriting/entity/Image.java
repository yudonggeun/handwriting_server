package com.promotion.handwriting.entity;

import javax.persistence.*;

@Entity
public class Image {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JoinColumn(name = "ad_fk")
    @ManyToOne(fetch = FetchType.LAZY)
    private Ad ad;
    @Column(name = "PRIORITY")
    private int priority;
    @Column(name = "IMAGE_NAME")
    private String imageName;

    protected Image() {
    }

    public Long getId() {
        return id;
    }

    public Image(Ad ad, int priority, String imageName) {
        this.ad = ad;
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
