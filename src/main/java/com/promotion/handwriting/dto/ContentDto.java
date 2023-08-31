package com.promotion.handwriting.dto;

import com.promotion.handwriting.dto.image.ImageDto;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class ContentDto {
    String id;
    String title;
    String description;
    List<ImageDto> images;

    @Builder
    private ContentDto(String id, String title, String description, List<ImageDto> images) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.images = images == null ? new LinkedList<>() : images;
    }
}
