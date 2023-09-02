package com.promotion.handwriting.dto.image;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * multipartFile을 통한 이미지 데이터
 */
@Data
public class MultipartImageDto {

    private MultipartFile multipartFile;

    public MultipartImageDto(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public static MultipartImageDto make(MultipartFile multipartFile){
        return new MultipartImageDto(multipartFile);
    }
}
