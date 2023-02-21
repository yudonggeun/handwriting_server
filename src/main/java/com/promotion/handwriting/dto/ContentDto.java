package com.promotion.handwriting.dto;

import com.promotion.handwriting.dto.parent.ImageDto;
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

    List<ImageDto> images = new LinkedList<>();

    public ContentDto() {
    }

    public static ContentDto convert(Ad ad) {
        if (ad == null) return null;
        if (ad.getType() != AdType.CONTENT) {
            throw new RuntimeException("Ad type is not intro. convert method need AdType.CONTENT ad");
        }
        ContentDto dto = new ContentDto();
        dto.setId(ad.getId() + "");
        dto.setTitle(ad.getTitle());
        dto.setDescription(ad.getDetail());

        dto.getImages().addAll(ad.getImages().stream()
                .map(image -> {
                    String originUrl = UrlUtil.getImageUrl(ad.getResourcePath(), image.getImageName());
                    String compressUrl = UrlUtil.getImageUrl(ad.getResourcePath(),
                            image.getCompressImageName() == null ? image.getImageName() : image.getCompressImageName());
                    return UrlImageDto.make(originUrl, compressUrl);
                })
                .collect(Collectors.toList()));

        return dto;
    }
}
