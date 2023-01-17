package com.promotion.handwriting.service;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.repository.AdRepository;
import com.promotion.handwriting.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl implements FileService {

    private final ResourceLoader loader;

    private final AdRepository adRepository;

    @Override
    public String saveIntroFile(MultipartFile file) throws IOException {
        Ad ad = adRepository.findIntroAd();
        Resource resource = loader.getResource(FileUtil.getImageResourcePath() + ad.getResourcePath());
        Path dir = resource.getFile().toPath();
        //파일 저장
        Path location = dir.resolve(file.getOriginalFilename());
        File saveFile = new File(location.toString());
        if(!saveFile.exists()) saveFile.createNewFile();
        file.transferTo(location);
        return file.getOriginalFilename();
    }

    @Override
    public String saveFile(MultipartFile file, long id) throws IOException {
        Ad ad = adRepository.findById(id).orElseThrow();
        Resource resource = loader.getResource(FileUtil.getImageResourcePath() + ad.getResourcePath());
        Path dir = resource.getFile().toPath();
        //파일 저장
        Path location = dir.resolve(file.getOriginalFilename());
        File saveFile = location.toFile();
        if(!saveFile.exists()) saveFile.createNewFile();
        file.transferTo(location);
        return file.getOriginalFilename();
    }

    @Override
    public List<String> saveFiles(List<MultipartFile> files, long id) throws IOException {

        List<String> result = new ArrayList<>(files.size());

        Ad ad = adRepository.findById(id).orElseThrow();
        Resource resource = loader.getResource(FileUtil.getImageResourcePath() + ad.getResourcePath());
        Path dir = resource.getFile().toPath();
        for (MultipartFile file : files) {
            //파일 저장
            Path location = dir.resolve(file.getOriginalFilename());
            File saveFile = location.toFile();
            if(!saveFile.exists()) saveFile.createNewFile();
            file.transferTo(location);
            result.add(file.getOriginalFilename());
        }

        return result;
    }

    @Override
    public String deleteFile(String fileName, long id) throws IOException {
        Ad ad = adRepository.findAdWithImagesById(id);
        Resource resource = loader.getResource(FileUtil.getImageResourcePath() + ad.getResourcePath());
        Path dir = resource.getFile().toPath();
        //파일 삭제
        File file = new File(dir.toAbsolutePath() + "/" + fileName);
        log.info(file.getAbsolutePath());
        if (file.exists()) {
            log.info("파일 삭제 : " + file.delete());
        }
        return fileName;
    }

    @Override
    public List<String> deleteFiles(List<String> fileNames, long id) throws IOException {

        Ad ad = adRepository.findAdWithImagesById(id);
        Resource resource = loader.getResource(FileUtil.getImageResourcePath() + ad.getResourcePath());
        Path dir = resource.getFile().toPath();
        //파일 삭제
        for (String fileName : fileNames) {
            File file = new File(dir.toAbsolutePath() + "/" + fileName);
            log.info(file.getAbsolutePath());
            if (file.exists()) {
                log.info("파일 삭제 : " + file.delete());
            }
        }

        return fileNames;
    }
}
