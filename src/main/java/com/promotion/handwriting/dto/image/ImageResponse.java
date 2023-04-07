package com.promotion.handwriting.dto.image;

import lombok.Data;

/**
 * this class is used to response image data.
 *
 * originUrl : origin file access url information. this file is not compressed. so this file size is bigger than simpleUrl.
 * simpleUrl : compressed file access url information. this file is compressed. if you want to display an image quickly, use it.
 */
@Data
public class ImageResponse implements ImageDto {
    String originUrl;
    String simpleUrl;

    public ImageResponse(String original, String compress) {
        this.originUrl = original;
        this.simpleUrl = compress;
    }

    public static ImageResponse make(String original, String compress){
        return new ImageResponse(original, compress);
    }
}
