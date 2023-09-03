package com.promotion.handwriting.dto.request;

import lombok.Data;

@Data
public class ChangeMainPageRequest {
    private String title;
    private String imageUrl;
    private String description;
}
