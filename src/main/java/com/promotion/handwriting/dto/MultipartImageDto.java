package com.promotion.handwriting.dto;

import com.promotion.handwriting.dto.parent.ImageDto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MultipartImageDto implements ImageDto {

    private MultipartFile multipartFile;

    public MultipartImageDto(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public static MultipartImageDto make(MultipartFile multipartFile){
        return new MultipartImageDto(multipartFile);
    }
}
