package com.promotion.handwriting.new_handwriting.dto;

import com.promotion.handwriting.new_handwriting.domain.type.ContentType;
import lombok.Data;

@Data
public class CreateContentRequest {
    private String title;
    private String description;
    private ContentType type;
}