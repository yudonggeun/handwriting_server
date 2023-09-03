package com.promotion.handwriting.dto;

import lombok.Data;

import java.util.List;

@Data
public class ContentDto {
    String id;
    String title;
    String description;
    List<ImageUrlDto> images;
}
