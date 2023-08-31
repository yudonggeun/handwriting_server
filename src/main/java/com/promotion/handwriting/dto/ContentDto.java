package com.promotion.handwriting.dto;

import com.promotion.handwriting.dto.image.ImageDto;
import com.promotion.handwriting.dto.image.UrlImageDto;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ContentDto {
    String id;
    String title;
    String description;
    List<ImageDto> images;

    @Builder
    private ContentDto(String id, String title, String description, List<ImageDto> images) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.images = images == null ? new LinkedList<>() : images;
    }

    //todo 리텍토리 필요: get(0) 오류 가능성 내포 추후 intro dto 객체 삭제 예정
    public IntroDto introDto(){
        IntroDto dto = new IntroDto();
        dto.setComments(Arrays.stream(description.split(IntroDto.separate)).collect(Collectors.toList()));
        UrlImageDto imageDto = (UrlImageDto) images.get(0);
        dto.setImage(imageDto.getOriginal());
        return dto;
    }
}
