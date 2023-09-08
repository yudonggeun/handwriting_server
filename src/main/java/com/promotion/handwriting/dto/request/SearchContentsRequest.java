package com.promotion.handwriting.dto.request;

import com.promotion.handwriting.enums.AdType;
import lombok.Data;

@Data
public class SearchContentsRequest {
    private AdType type = AdType.CONTENT;
}
