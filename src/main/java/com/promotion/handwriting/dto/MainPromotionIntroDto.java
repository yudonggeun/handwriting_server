package com.promotion.handwriting.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class MainPromotionIntroDto {
    String image;
    List<String> comments = new LinkedList<>();
}
