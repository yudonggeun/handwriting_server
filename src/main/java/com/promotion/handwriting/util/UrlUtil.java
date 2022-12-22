package com.promotion.handwriting.util;

import com.promotion.handwriting.entity.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlUtil {
    private static String defaultUrl;
    private static String imageUrl;

    @Value("${url.default}")
    public void setDefaultUrl(String defaultUrl) {
        UrlUtil.defaultUrl = defaultUrl;
    }

    @Value("${url.image}")
    public void setImageUrl(String imageUrl) {
        UrlUtil.imageUrl = imageUrl;
    }

    public static String getDefaultUrl() {
        return defaultUrl;
    }

    public static String getImageUrl(String directory, Image image) {
        return imageUrl + directory + "/" + image.getImageName();
    }
}
