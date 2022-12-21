package com.promotion.handwriting.service;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.AdRepository;
import com.promotion.handwriting.util.FileUtil;
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

    private final String directoryPath = FileUtil.getFileResourcePath();

    private final AdRepository adRepository;

    @Override
    public String saveIntroFile(MultipartFile file) throws IOException {
        Ad ad = adRepository.findByType(AdType.INTRO);
        Resource resource = loader.getResource(directoryPath + ad.getResourcePath());
        Path dir = resource.getFile().toPath();
        //파일 저장
        Path location = dir.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);

        return file.getOriginalFilename();
    }

    @Override
    public String saveFile(MultipartFile file, long id) throws IOException {

        Ad ad = adRepository.findById(id).orElseThrow();
        Resource resource = loader.getResource(directoryPath + ad.getResourcePath());
        Path dir = resource.getFile().toPath();
        //파일 저장
        Path location = dir.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);

        return file.getOriginalFilename();
    }

    @Override
    public String deleteFile(String fileName, long id) throws IOException {
        Ad ad = adRepository.findById(id).orElseThrow();
        Resource resource = loader.getResource(directoryPath + ad.getResourcePath());
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
