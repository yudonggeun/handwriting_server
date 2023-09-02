package com.promotion.handwriting.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class IntroDto {

    private String title;
    private String imageUrl;

    private String description;

    public IntroDto(String title, String imageUrl, String description) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
