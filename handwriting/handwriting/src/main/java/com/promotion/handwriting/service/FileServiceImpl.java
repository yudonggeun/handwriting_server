package com.promotion.handwriting.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Override
    public String saveFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        //확장자 추출
        int index = fileName.lastIndexOf('.');
        if(index == -1){
            throw new RuntimeException("파일에 확장자가 없습니다.");
        }

        ClassPathResource resource = new ClassPathResource("/static/image/intro");
        Path dir = resource.getFile().toPath();
        //파일 저장
        Path location = dir.resolve(file.getOriginalFilename());

        Files.copy(file.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}
