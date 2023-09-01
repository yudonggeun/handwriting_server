package com.promotion.handwriting;

import com.promotion.handwriting.repository.file.LocalFileRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Value("${directory.root}")
    private String resourcePath;
    @Value("${directory.image}")
    private String imagePath;

    @SneakyThrows
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String os = System.getProperty("os.name").toLowerCase();
        var fileResourcePath = (os.contains("win") ? "file:///" : "file:") + resourcePath;
        if (fileResourcePath.charAt(fileResourcePath.length() - 1) == '/') {
            fileResourcePath = fileResourcePath.substring(0, fileResourcePath.length() - 1);
        }
        registry.addResourceHandler("image/**")
                .addResourceLocations(fileResourcePath + imagePath + "/image/", "classpath:/static/image/");
    }


}
