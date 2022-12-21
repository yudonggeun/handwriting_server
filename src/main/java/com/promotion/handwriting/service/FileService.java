package com.promotion.handwriting.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String saveIntroFile(MultipartFile file) throws IOException;

    String saveFile(MultipartFile file, long id) throws IOException;

    String deleteFile(String fileName, long id) throws IOException;
}
