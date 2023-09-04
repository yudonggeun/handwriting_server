package com.promotion.handwriting.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeleteImageRequest {
    Long contentId;
    List<Long> imageIds = new ArrayList<>();
}
