package com.promotion.handwriting.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateContentRequest {
    String title;
    String description;
    String path = "/content/" + UUID.randomUUID();
}
