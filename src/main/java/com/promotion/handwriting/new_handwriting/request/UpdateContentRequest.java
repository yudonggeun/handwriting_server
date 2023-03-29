package com.promotion.handwriting.new_handwriting.request;

import lombok.Data;

@Data
public class UpdateContentRequest {
    private String contentId;
    private String title;
    private String description;
}
