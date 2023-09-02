package com.promotion.handwriting.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ImageDeleteRequest {
    List<String> files = new ArrayList<>();
}
