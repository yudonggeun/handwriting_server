package com.promotion.handwriting.new_handwriting.dto;

import com.promotion.handwriting.new_handwriting.domain.type.ContentType;
import lombok.Data;

@Data
public class ContentCreateDto {
    private String title;
    private String description;
    private ContentType type;

    public String getType() {
        return type.name();
    }
}