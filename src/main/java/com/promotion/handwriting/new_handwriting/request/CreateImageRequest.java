package com.promotion.handwriting.new_handwriting.request;

import lombok.Data;

@Data
public class CreateImageRequest {

    private String originName;
    private String compressName;
    private String path;
    private String contentId;
}
