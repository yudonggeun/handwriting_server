package com.promotion.handwriting.dto;

import lombok.Data;

import java.util.List;

@Data
public class ContentDto {
    String id;
    String title;
    String description;
    List<Object> images; // todo object 리스트 변경 필요
}
