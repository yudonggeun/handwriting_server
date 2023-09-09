package com.promotion.handwriting.dto;

import com.promotion.handwriting.WebConfig;
import lombok.Data;

/**
 * 이미지의 compress iamge 경로와 original 경로를 나타내는 DTO
 */
@Data
public class ImageUrlDto {

    private long id;
    private String original;
    private String compress;

    public ImageUrlDto(long id, String original, String compress) {
        this.id = id;
        this.original = original;
        this.compress = compress;
    }
}
