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

    private final String resourcePath = System.getProperty("user.dir") + "\\handwriting_resources\\image";

    @SneakyThrows
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String connectPath = "image/**";
        String fileResource = (System.getProperty("os.name").toLowerCase().contains("win") ?
                "file:///" :
                "file:/") + resourcePath + "\\";
        makeResourceFile();
        registry.addResourceHandler(connectPath)
                .addResourceLocations(fileResource);
    }

    private void makeResourceFile() throws IOException {
        try {
            createDirectories(Paths.get(resourcePath + "\\intro"));
            createDirectories(Paths.get(resourcePath + "\\content"));
        } catch (IOException e) {
            log.error("create resource directory fail");
            e.printStackTrace();
            throw e;
        }
        log.info("create resource directory : [resource path=" + resourcePath + "]");
    }
}
