package com.promotion.handwriting.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String saveFile(MultipartFile file, String path) throws IOException;

    String deleteFile(String fileName, String path) throws IOException;
}
