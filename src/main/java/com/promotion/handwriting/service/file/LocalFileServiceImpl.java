package com.promotion.handwriting.service.file;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.repository.AdRepository;
import com.promotion.handwriting.util.FileUtil;
import com.promotion.handwriting.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Primary
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LocalFileServiceImpl implements FileService {

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
        if (!saveFile.exists()) saveFile.createNewFile();
        file.transferTo(location);
        return file.getOriginalFilename();
    }

    @Override
    public String saveContentFile(MultipartFile file, String resourcePath) throws IOException {
        Resource resource = loader.getResource(FileUtil.getImageResourcePath() + resourcePath);
        Path dir = resource.getFile().toPath();
        //파일 저장
        Path location = dir.resolve(file.getOriginalFilename());
        File saveFile = location.toFile();

        log.info("MultipartFile size : " + file.getSize());
        if (!saveFile.exists()) saveFile.createNewFile();
        file.transferTo(location);
        return file.getOriginalFilename();
    }

    @Override
    public List<String> saveFiles(List<MultipartFile> files, String resourcePath) throws IOException {

        List<String> result = new ArrayList<>(files.size());

        Resource resource = loader.getResource(FileUtil.getImageResourcePath() + resourcePath);
        Path dir = resource.getFile().toPath();
        for (MultipartFile file : files) {
            //파일 저장
            Path location = dir.resolve(file.getOriginalFilename());
            File saveFile = location.toFile();
            if (!saveFile.exists()) saveFile.createNewFile();
            file.transferTo(location);
            result.add(file.getOriginalFilename());
        }

        return result;
    }

    @Override
    public String deleteFile(String fileName, String resourcePath) throws IOException {
        Resource resource = loader.getResource(FileUtil.getImageResourcePath() + resourcePath);
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
    public List<String> deleteFiles(List<String> fileNames, String resourcePath) throws IOException {
        Resource resource = loader.getResource(FileUtil.getImageResourcePath() + resourcePath);
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

    @Override
    public void deleteDirectory(String resourcePath) throws IOException {
        Resource resource = loader.getResource(FileUtil.getImageResourcePath() + resourcePath);
        File directory = resource.getFile();
        FileUtil.deleteDirectory(directory);
    }

    @Override
    public List<String> compressFiles(List<String> fileNames, String resourcePath) throws IOException {

        List<String> result = new ArrayList<>(fileNames.size());

        Resource resource = loader.getResource(FileUtil.getImageResourcePath() + resourcePath);
        String directoryPath = resource.getFile().getAbsolutePath();

        for (String fileName : fileNames) {
            File originalFile = new File(directoryPath + "/" + fileName);
            //저장된 파일
            if(!originalFile.exists()) continue;
            String compressFileName = "compress-" + originalFile.getName();
            ImageUtil.compress(originalFile.getAbsolutePath(), directoryPath + "/" + compressFileName);

            log.info("compressFileName : " + compressFileName);
            result.add(compressFileName);
        }
        return result;
    }

    @Override
    public String compressFile(String fileName, String resourcePath) throws IOException {
        Resource resource = loader.getResource(FileUtil.getImageResourcePath() + resourcePath);
        Path dir = resource.getFile().toPath();
        //origin file load
        Path location = dir.resolve(fileName);
        File originalFile = location.toFile();

        if (!originalFile.exists()) return null;
        String compressFileName = "compress-" + fileName;
        String directoryPath = resource.getFile().getAbsolutePath();
        ImageUtil.compress(originalFile.getAbsolutePath(), directoryPath + "/" + compressFileName);
        log.info("compressFileName : " + compressFileName);

        return compressFileName;
    }
}
