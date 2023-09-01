package com.promotion.handwriting.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateContentRequest {
    String id;
    String title;
    String description;

    @Builder
    private CreateContentRequest(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
