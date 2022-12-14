package com.promotion.handwriting.service;

import com.promotion.handwriting.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final ResourceLoader loader;

    private final String directoryPath = FileUtils.getFileResourcePath();

    @Override
    public String saveFile(MultipartFile file, String path) throws IOException {
        String fileName = file.getOriginalFilename();

        //확장자 추출
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            throw new RuntimeException("파일에 확장자가 없습니다.");
        }

        Resource resource = loader.getResource(directoryPath + "/" + path);
        Path dir = resource.getFile().toPath();
        //파일 저장
        Path location = dir.resolve(file.getOriginalFilename());

        Files.copy(file.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    @Override
    public String deleteFile(String fileName, String path) throws IOException {

        //확장자 추출
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            throw new RuntimeException("파일에 확장자가 없습니다.");
        }

        Resource resource = loader.getResource(directoryPath + "/" + path);
        Path dir = resource.getFile().toPath();
        //파일 삭제
        File file = new File(dir.toAbsolutePath() + "/" + fileName);
        log.info(file.getAbsolutePath());
        if (file.exists()) {
            log.info("파일 삭제 : " + file.delete());
        }
        return fileName;
    }
}
