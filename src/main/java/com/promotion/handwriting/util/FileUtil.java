package com.promotion.handwriting.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;

import static java.nio.file.Files.createDirectories;

/**
 * this class has to use in the method. do not use in constructor.
 */
@Slf4j
@Component
public class FileUtil {

    private static String fileResourcePath;
    private static String resourcePath;
    private static String rootPath;
    private static String imagePath;

    @Value("#{systemProperties['user.dir']}")
    public void setResourcePath(String resourcePath) {
        FileUtil.resourcePath = resourcePath;
    }

    @Value("${directory.root}")
    public void setRootPath(String rootPath) throws IOException {
        FileUtil.rootPath = rootPath;
        FileUtil.createDirectoryAtPath(FileUtil.rootPath);
    }

    @Value("${directory.image}")
    public void setImagePath(String imagePath) throws IOException {
        FileUtil.imagePath = imagePath;
        FileUtil.createDirectoryAtPath(FileUtil.imagePath);
    }

    public static String getFileResourcePath() {

        if (fileResourcePath != null) return fileResourcePath;

        String os = System.getProperty("os.name").toLowerCase();

        fileResourcePath = (os.contains("win") ? "file:///" : "file:") +
                resourcePath +
                (resourcePath.endsWith("/") ? rootPath.substring(1) : rootPath);

        return fileResourcePath;
    }

    /**
     * create directory in resource directory.
     *
     * @param path must start with "/"
     */
    public static void createDirectoryAtPath(String path) throws IOException {
        try {
            createDirectories(Paths.get(resourcePath + path));
        } catch (IOException e) {
            log.error("create entity resource directory : fail");
            e.printStackTrace();
            throw e;
        }
        log.info("create entity resource directory : [resource path=" + resourcePath + path + "]");
    }

    /**
     * create directory in image resource directory.
     *
     * @param path must start with "/"
     */
    public static void createImageDirectory(String path) throws IOException {
        createDirectoryAtPath(imagePath + path);
    }
}
