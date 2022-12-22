package com.promotion.handwriting;

import com.promotion.handwriting.util.FileUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @SneakyThrows
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String connectPath = "image/**";
        String[] fileResource = {
                FileUtil.getFileResourcePath() + "/image/",
                "classpath:/static/image/"
        };

        registry.addResourceHandler(connectPath)
                .addResourceLocations(fileResource);
    }


}
