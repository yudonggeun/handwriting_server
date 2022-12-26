package com.promotion.handwriting.dto;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.util.UrlUtil;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class IntroDto {
    String image;
    List<String> comments;

    public static String separate = "#";

    public IntroDto() {
    }

    public static IntroDto convert(Ad ad){
        if(ad.getType() != AdType.INTRO){
            throw new RuntimeException("ad type 이 AdType.INTRO 이어야 합니다.");
        }

        IntroDto dto = new IntroDto();
        dto.setComments(Arrays.stream(ad.getDetail().split(IntroDto.separate)).collect(Collectors.toList()));
        dto.setImage(UrlUtil.getImageUrl(ad.getResourcePath(), ad.getImages().get(0)));
        return dto;
    }
}
