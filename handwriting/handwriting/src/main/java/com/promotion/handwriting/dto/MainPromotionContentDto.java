package com.promotion.handwriting.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class MainPromotionContentDto {
    String id;
    String title;
    String description;
    List<String> images = new LinkedList<>();

    public boolean addImage(String fileName){
        return images.add(fileName);
    }
}
