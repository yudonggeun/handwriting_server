package com.promotion.handwriting.dto;

import lombok.Data;

import java.util.List;

@Data
public class IntroDto {
    String image;
    List<String> comments;
    public static String separate = "#";
}
