package com.promotion.handwriting;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Paths;

import static java.nio.file.Files.createDirectories;

@Slf4j
@Getter
@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Value("#{systemProperties['user.dir']}")
    private String projectRootPath;
    @Value("${directory.root}")
    private String resourcePath;
    @Value("${directory.image}")
    private String imagePath;
    @Value("${spring.url.gatewayPath}")
    private String gatewayPath;

    private String fileResourcePath;

    public String getImageDirectory() {
        return projectRootPath + imagePath;
    }

    @PostConstruct
    private void init() throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        fileResourcePath = (os.contains("win") ? "file:///" : "file:") + projectRootPath;
        if (fileResourcePath.charAt(fileResourcePath.length() - 1) == '/') {
            fileResourcePath = fileResourcePath.substring(0, fileResourcePath.length() - 1);
        }
        createDirectories(Paths.get(projectRootPath + resourcePath));
        createDirectories(Paths.get(projectRootPath + imagePath));
    }

    private String imageUrl = "image";

    @SneakyThrows
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("file path: " + fileResourcePath + imagePath);
        registry.addResourceHandler(imageUrl + "/**")
                .addResourceLocations(fileResourcePath + imagePath + "/", "classpath:/static/image/");
    }

    public String getImageUrl() {
        return gatewayPath + "/" + imageUrl;
    }
}
