package com.promotion.handwriting.service;

import com.promotion.handwriting.controller.DataController;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.AdRepository;
import com.promotion.handwriting.service.file.FileService;
import com.promotion.handwriting.util.FileUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class FileServiceTest {

    @Autowired
    FileService fileService;
    @Autowired
    AdRepository adRepository;
    @Autowired
    ResourceLoader loader;
    @Autowired
    DataController dataController;

    Ad ad;
    ClassPathResource resource = new ClassPathResource("static/image/no_image.jpg");
    MockMultipartFile file = new MockMultipartFile("image",
            "test.png",
            "image/png",
            new FileInputStream(resource.getFile()));

    FileServiceTest() throws IOException {
    }

    @BeforeEach
    void inputData() throws IOException {
        ad = Ad.createAd(AdType.INTRO, "", "소개입니다.", "/test_file");
        adRepository.save(ad);
    }

    @AfterEach
    void deleteData() {
        if (ad != null)
            adRepository.delete(ad);
    }

    @Test
    void saveIntroFile() throws IOException {
        fileService.saveIntroFile(file);
        fileService.deleteFile(file.getOriginalFilename(), ad.getId());

        String imageResourcePath = FileUtil.getImageResourcePath();
        Resource resource = loader.getResource(imageResourcePath + ad.getResourcePath());
        Path path = resource.getFile().toPath();
        File deleteFile = path.resolve(this.file.getOriginalFilename()).toFile();
        assertThat(deleteFile.exists()).isFalse();
    }

    @Test
    void saveFileAndDeleteFile() throws IOException {
        fileService.saveContentFile(file, ad.getId());
        fileService.deleteFile(file.getOriginalFilename(), ad.getId());

        String imageResourcePath = FileUtil.getImageResourcePath();
        Resource resource = loader.getResource(imageResourcePath + ad.getResourcePath());
        Path path = resource.getFile().toPath();
        File deleteFile = path.resolve(this.file.getOriginalFilename()).toFile();
        assertThat(deleteFile.exists()).isFalse();
    }

    @Test
    void deleteFile() throws IOException {

        //save File
        Ad contentAd = Ad.createAd(AdType.CONTENT, "test Content", "소개입니다.", "/test_file");
        contentAd = adRepository.save(contentAd);
        fileService.saveContentFile(file, contentAd.getId());

        //delete File
        List<String> fileList = new LinkedList<>();
        fileList.add(file.getOriginalFilename());
        fileService.deleteFiles(fileList, contentAd.getId());
        //assert
        String imageResourcePath = FileUtil.getImageResourcePath();
        Resource resource = loader.getResource(imageResourcePath + contentAd.getResourcePath());
        Path path = resource.getFile().toPath();
        File deletedFile = path.resolve(file.getOriginalFilename()).toFile();
        assertThat(deletedFile.exists()).isFalse();
    }
}