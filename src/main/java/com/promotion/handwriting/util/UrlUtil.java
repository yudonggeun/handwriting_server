package com.promotion.handwriting.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlUtil {
    private static String defaultUrl;
    private static String imageUrl;

    @Value("${spring.url.default}")
    public void setDefaultUrl(String defaultUrl) {
        UrlUtil.defaultUrl = defaultUrl;
    }

    @Value("${spring.url.image}")
    public void setImageUrl(String imageUrl) {
        UrlUtil.imageUrl = imageUrl;
    }

    public static String getDefaultUrl() {
        return defaultUrl;
    }

    public static String getImageUrl(String directory, String imageName) {
        return imageUrl + directory + "/" + imageName;
    }

    public static String removeUrlPath(String imageUrl){
        int startIndex = imageUrl.lastIndexOf("/") + 1;
        return imageUrl.substring(startIndex);
    }
}
