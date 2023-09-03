package com.promotion.handwriting.dto.request;

import lombok.Data;

@Data
public class ChangeContentRequest {
    long id;
    String description;
    String title;
}
