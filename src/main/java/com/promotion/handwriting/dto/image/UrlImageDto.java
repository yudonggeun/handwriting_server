package com.promotion.handwriting.dto.image;

import lombok.Data;

/**
 * 이미지의 compress iamge 경로와 original 경로를 나타내는 DTO
 */
@Data
public class UrlImageDto {
    String original;
    String compress;

    public UrlImageDto(String original, String compress) {
        this.original = original;
        this.compress = compress;
    }

    public static UrlImageDto make(String original, String compress){
        return new UrlImageDto(original, compress);
    }
}
