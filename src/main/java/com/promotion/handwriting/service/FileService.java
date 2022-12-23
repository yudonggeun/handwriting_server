package com.promotion.handwriting.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface FileService {
    String saveIntroFile(MultipartFile file) throws IOException;

    String saveFile(MultipartFile file, long id) throws IOException;

    void saveFiles(List<MultipartFile> files, long id) throws IOException;

    String deleteFile(String fileName, long id) throws IOException;

    void deleteFiles(Set<String> fileNames, long id) throws IOException;
}
