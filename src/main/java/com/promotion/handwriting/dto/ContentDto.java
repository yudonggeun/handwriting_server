package com.promotion.handwriting.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class ContentDto {
    String id;
    String title;
    String description;
    List<String> images = new LinkedList<>();

    public ContentDto(){}

    public boolean addImage(String fileName) {
        return images.add(fileName);
    }
}
