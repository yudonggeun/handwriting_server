package com.promotion.handwriting.dto;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.enums.AdType;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MainPromotionContentDto {
    String id;
    String title;
    String description;
    List<String> images = new LinkedList<>();

    public MainPromotionContentDto(Ad ad) {
        if (ad.getType() != AdType.CONTENT) {
            throw new IllegalArgumentException("AD 객체의 type이 CONTENT이어야 합니다.");
        }
        id = String.valueOf(ad.getId());
        title = ad.getTitle();
        description = ad.getDetail();
        images.addAll(ad.getImages().stream().map(image -> "/api" + ad.getResourcePath() + image.getImageName()).collect(Collectors.toList()));
    }

    public boolean addImage(String fileName) {
        return images.add(fileName);
    }
}
