package com.promotion.handwriting.dto;

import lombok.Data;

@Data
public class MainPageDto {

    private String title;
    private String imageUrl;
    private String description;

    public MainPageDto(String title, String imageUrl, String description) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
