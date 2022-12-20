package com.promotion.handwriting.entity;

import javax.persistence.*;

@Entity
public class Image {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "PRIORITY")
    private int priority;
    @Column(name = "IMAGE_NAME")
    private String imageName;

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
