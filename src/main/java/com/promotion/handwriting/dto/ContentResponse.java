package com.promotion.handwriting.dto;

import com.promotion.handwriting.dto.image.ImageDto;
import com.promotion.handwriting.dto.image.ImageResponse;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.util.UrlUtil;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ContentResponse {
    String id;
    String title;
    String description;

    List<ImageDto> images = new LinkedList<>();

    public ContentResponse() {
    }

    public static ContentResponse convert(Ad ad) {
        if (ad == null) return null;
        if (ad.getType() != AdType.CONTENT) {
            throw new RuntimeException("Ad type is not intro. convert method need AdType.CONTENT ad");
        }
        ContentResponse dto = new ContentResponse();
        dto.setId(ad.getId() + "");
        dto.setTitle(ad.getTitle());
        dto.setDescription(ad.getDetail());

        dto.getImages().addAll(ad.getImages().stream()
                .map(image -> {
                    String originUrl = UrlUtil.getImageUrl(ad.getResourcePath(), image.getImageName());
                    String compressUrl = UrlUtil.getImageUrl(ad.getResourcePath(),
                            image.getCompressImageName() == null ? image.getImageName() : image.getCompressImageName());
                    return ImageResponse.make(originUrl, compressUrl);
                })
                .collect(Collectors.toList()));

        return dto;
    }
}
