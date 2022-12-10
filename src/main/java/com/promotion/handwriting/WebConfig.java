package com.promotion.handwriting;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Paths;

import static java.nio.file.Files.createDirectories;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private String connectPath = "/**";
    private String resourcePath = "file:///home/uploadedImage";//TODO

    @SneakyThrows
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        makeResourceFile();
        registry.addResourceHandler(connectPath)
                .addResourceLocations(resourcePath);
    }

    private void makeResourceFile() throws IOException {
        log.info("create resource directory start");
        String rootPath = System.getProperty("user.dir");
        String resourcePath = "\\handwriting_resources";
        try {
            createDirectories(Paths.get(rootPath + resourcePath + "\\image\\intro"));
            createDirectories(Paths.get(rootPath + resourcePath + "\\image\\content"));
        } catch (IOException e) {
            log.error("create resource directory fail");
            e.printStackTrace();
            throw e;
        }
        log.info("create resource directory end : [resource path=" + rootPath + resourcePath + "]");
    }
}
