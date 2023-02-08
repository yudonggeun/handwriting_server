package com.promotion.handwriting.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.Files.createDirectories;

/**
 * this class has to use in the method. do not use in constructor.
 */
@Slf4j
@Component
public class FileUtil {

    private static String fileResourcePath;
    private static String resourcePath;
    private static String projectRootPath;
    private static String imagePath;

    @Value("#{systemProperties['user.dir']}")
    public void setResourcePath(String resourcePath) {
        FileUtil.resourcePath = resourcePath;
    }

    @Value("${directory.root}")
    public void setRootPath(String rootPath) throws IOException {
        FileUtil.projectRootPath = rootPath;
        FileUtil.createDirectoryAtPath(FileUtil.projectRootPath);
    }

    @Value("${directory.image}")
    public void setImagePath(String imagePath) throws IOException {
        FileUtil.imagePath = imagePath;
        FileUtil.createDirectoryAtPath(FileUtil.imagePath);
    }

    private static void setFileResourcePath(){
        String os = System.getProperty("os.name").toLowerCase();
        fileResourcePath = (os.contains("win") ? "file:///" : "file:") + resourcePath;
        if(fileResourcePath.charAt(fileResourcePath.length()-1) == '/'){
            fileResourcePath = fileResourcePath.substring(0, fileResourcePath.length()-1);
        }
    }

    public static String getFileResourcePath() {
        if (fileResourcePath != null) return fileResourcePath + projectRootPath;
        setFileResourcePath();
        return fileResourcePath + projectRootPath;
    }

    public static String getImageResourcePath() {
        if (fileResourcePath != null) return fileResourcePath + imagePath;
        setFileResourcePath();
        return fileResourcePath + imagePath;
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

    public static File covertFile(MultipartFile file, String fileName) throws IOException {
        File uploadFile = new File(fileName);
        file.transferTo(uploadFile);
        return uploadFile;
    }

    public static void deleteDirectory(File directory) {
        // Get a list of all the files in the directory
        File[] files = directory.listFiles();

        // If the directory is not empty, delete its contents
        if (files != null) {
            for (File file : files) {
                // If the file is a directory, recursively delete its contents
                if (file.isDirectory()) {
                    deleteDirectory(file);
                }
                // Otherwise, delete the file
                else {
                    file.delete();
                }
            }
        }

        // Delete the empty directory
        directory.delete();
    }
}
