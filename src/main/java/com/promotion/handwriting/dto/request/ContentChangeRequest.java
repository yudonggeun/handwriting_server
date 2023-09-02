package com.promotion.handwriting.dto.request;

import lombok.Data;

@Data
public class ContentChangeRequest {
    long id;
    String description;
    String title;
}
