package com.promotion.handwriting.service;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.AdRepository;
import com.promotion.handwriting.repository.ImageRepository;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl implements FileService {

    private final ResourceLoader loader;

    private final AdRepository adRepository;
    private final ImageRepository imageRepository;

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
        //파일 db 저장
        Image image = new Image(ad, Integer.MAX_VALUE, file.getOriginalFilename());
        imageRepository.save(image);
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
        //파일 db 저장
        Image image = new Image(ad, Integer.MAX_VALUE, file.getOriginalFilename());
        imageRepository.save(image);
        return file.getOriginalFilename();
    }

    @Override
    public void saveFiles(List<MultipartFile> files, long id) throws IOException {
        Ad ad = adRepository.findById(id).orElseThrow();
        Resource resource = loader.getResource(FileUtil.getImageResourcePath() + ad.getResourcePath());
        Path dir = resource.getFile().toPath();
        for (MultipartFile file : files) {
            //파일 저장
            Path location = dir.resolve(file.getOriginalFilename());
            File saveFile = location.toFile();
            if(!saveFile.exists()) saveFile.createNewFile();
            file.transferTo(location);
            //파일 db 저장
            Image image = new Image(ad, Integer.MAX_VALUE, file.getOriginalFilename());
            imageRepository.save(image);
        }
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
        //파일 db 삭제
        imageRepository.deleteByImageNameAndAd(fileName, ad);
        return fileName;
    }

    @Override
    public void deleteFiles(Set<String> fileNames, long id) throws IOException {
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
        //파일 db 삭제
        List<Image> deleteImage = ad.getImages().stream()
                .filter(image -> fileNames.contains(image.getImageName()))
                .collect(Collectors.toList());
        imageRepository.deleteAllInBatch(deleteImage);
    }
}
