package com.promotion.handwriting.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Paths;

import static java.nio.file.Files.createDirectories;

@Slf4j
public class FileUtils {

    private static String path;
    private static final String resourcePath = System.getProperty("user.dir");
    private static final String imagePath = "/handwriting_resources/image";
    private static final String textPath = "/handwriting_resources/text";
    private static final String introPath = "/intro";
    private static final String contentPath = "/content";

    static public String getFileResourcePath() {

        if (path != null) return path;

        String os = System.getProperty("os.name").toLowerCase();
        String directory = System.getProperty("user.dir");
        String fileDirectory = "handwriting_resources";

        path = (os.contains("win") ? "file:///" : "file:") +
                directory +
                (directory.endsWith("/") ? "" : "/") +
                fileDirectory;

        return path;
    }

    public static void makeResourceFile() throws IOException {
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
