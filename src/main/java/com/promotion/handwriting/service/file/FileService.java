package com.promotion.handwriting.service.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    String saveIntroFile(MultipartFile file) throws IOException;

    String saveContentFile(MultipartFile file, long id) throws IOException;

    List<String> saveFiles(List<MultipartFile> files, long id) throws IOException;

    String deleteFile(String fileName, long id) throws IOException;

    List<String> deleteFiles(List<String> fileNames, long id) throws IOException;

    List<String> compressFiles(List<String> fileNames, long id) throws IOException;

    String compressFile(String fileName, long id) throws IOException;
}
