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
    List<String> originImages = new LinkedList<>();
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
        //원본 이미지 경로
        List<String> originImages = ad.getImages().stream()
                .map(image -> UrlUtil.getImageUrl(ad.getResourcePath(), image.getImageName()))
                .collect(Collectors.toList());
        dto.setOriginImages(originImages);

        //압축 이미지 경로
        List<String> compressImages = ad.getImages().stream()
                .map(image -> {
                    //압축 이미지가 없으면 원본 반환
                    String imageName = image.getCompressImageName() == null ? image.getImageName() : image.getCompressImageName();
                    return UrlUtil.getImageUrl(ad.getResourcePath(), imageName);
                })
                .collect(Collectors.toList());
        dto.setImages(compressImages);

        return dto;
    }
}
