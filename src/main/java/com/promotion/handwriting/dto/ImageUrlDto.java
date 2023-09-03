package com.promotion.handwriting.dto;

import lombok.Data;

/**
 * 이미지의 compress iamge 경로와 original 경로를 나타내는 DTO
 */
@Data
public class ImageUrlDto {
    String original;
    String compress;

    public ImageUrlDto(String original, String compress) {
        this.original = original;
        this.compress = compress;
    }

    public static ImageUrlDto make(String original, String compress){
        return new ImageUrlDto(original, compress);
    }
}
