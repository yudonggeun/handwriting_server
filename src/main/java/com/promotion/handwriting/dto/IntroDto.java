package com.promotion.handwriting.dto;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
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

    static String separate = "#";

    public IntroDto() {
    }

    public IntroDto(Ad ad) {
        if (ad.getType() != AdType.INTRO) {
            throw new IllegalArgumentException("AD 객체의 type 이 INTRO 이어야 합니다.");
        }
        comments = Arrays.stream(ad.getDetail().split(separate)).collect(Collectors.toList());
        List<Image> images = ad.getImages();
        if(images.size() == 1){
            image = UrlUtil.getImageUrl(ad.getResourcePath(), images.get(0));
        }
    }

    public String getComment(){
        StringBuilder sb = new StringBuilder();
        for (String comment : this.getComments())
            sb.append(comment).append(separate);
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
