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

    private final String resourcePath = System.getProperty("user.dir");
    private final String operateSystem = System.getProperty("os.name").toLowerCase();
    @SneakyThrows
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String connectPath = "image/**";
        String filePath = (operateSystem.contains("win") ? "file:///" : "file:") + resourcePath + "/handwriting_resources/image/";
        String[] fileResource = {
                filePath,
                "classpath:/static/image/"
        };

        makeResourceFile();

        registry.addResourceHandler(connectPath)
                .addResourceLocations(fileResource);
    }

    private void makeResourceFile() throws IOException {
        String imagePath = "/handwriting_resources/image";
        String textPath = "/handwriting_resources/text";
        String introPath = "/intro";
        String contentPath = "/content";

        try {
            createDirectories(Paths.get(resourcePath + textPath + introPath));
            createDirectories(Paths.get(resourcePath + textPath + contentPath));
            createDirectories(Paths.get(resourcePath + imagePath + introPath));
            createDirectories(Paths.get(resourcePath + imagePath + contentPath));
        } catch (IOException e) {
            log.error("create resource directory fail");
            e.printStackTrace();
            throw e;
        }
        log.info("create resource directory : [resource path=" + resourcePath + "]");
    }
}
