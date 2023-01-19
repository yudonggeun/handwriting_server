package com.promotion.handwriting.dto;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.util.UrlUtil;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ContentDto {
    String id;
    String title;
    String description;
    List<String> images = new LinkedList<>();

    public ContentDto(){}

    public boolean addImage(String fileName) {
        return images.add(fileName);
    }

    public static ContentDto convert(Ad ad){
        if(ad == null) return null;
        if(ad.getType() != AdType.CONTENT){
            throw new RuntimeException("Ad type is not intro. convert method need AdType.CONTENT ad");
        }
        ContentDto dto = new ContentDto();
        dto.setId(ad.getId()+"");
        dto.setTitle(ad.getTitle());
        dto.setDescription(ad.getDetail());
        List<String> images = ad.getImages().stream()
                .map(image -> UrlUtil.getImageUrl(ad.getResourcePath(), image))
                .collect(Collectors.toList());
        dto.setImages(images);
        return dto;
    }
}
