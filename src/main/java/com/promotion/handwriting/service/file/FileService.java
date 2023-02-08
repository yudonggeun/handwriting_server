package com.promotion.handwriting.service.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    String saveIntroFile(MultipartFile file) throws IOException;

    String saveContentFile(MultipartFile file, String resourcePath) throws IOException;

    List<String> saveFiles(List<MultipartFile> files, String resourcePath) throws IOException;

    String deleteFile(String fileName, String resourcePath) throws IOException;

    List<String> deleteFiles(List<String> fileNames, String resourcePath) throws IOException;

    List<String> compressFiles(List<String> fileNames, String resourcePath) throws IOException;

    String compressFile(String fileName, String resourcePath) throws IOException;

    void deleteDirectory(String resourcePath) throws IOException;
}
