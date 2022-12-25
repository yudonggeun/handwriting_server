package com.promotion.handwriting.service;

import com.promotion.handwriting.controller.DataController;
import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.DeleteFileDto;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.AdRepository;
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
    DataService dataService;
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
        ad = new Ad(AdType.INTRO, "", "소개입니다.", "/test_file", null);
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
        fileService.saveFile(file, ad.getId());
        fileService.deleteFile(file.getOriginalFilename(), ad.getId());

        String imageResourcePath = FileUtil.getImageResourcePath();
        Resource resource = loader.getResource(imageResourcePath + ad.getResourcePath());
        Path path = resource.getFile().toPath();
        File deleteFile = path.resolve(this.file.getOriginalFilename()).toFile();
        assertThat(deleteFile.exists()).isFalse();
    }

    @Test
    void deleteFile() throws IOException {

        Ad contentAd = new Ad(AdType.CONTENT, "test Content", "소개입니다.", "/" + "test_file", null);
        adRepository.save(contentAd);
        fileService.saveFile(file, contentAd.getId());
        List<ContentDto> dtos = dataService.getContentDtos();
        ContentDto target = null;
        for (ContentDto dto : dtos) {
            if(Long.parseLong(dto.getId()) == contentAd.getId()){
                target = dto;
                break;
            }
        }
        assertThat(target).isNotNull();
        List<String> images = target.getImages();
        String deleteFile = null;
        for (String image : images) {
            if(image.contains(file.getOriginalFilename())){
                deleteFile = image;
                System.out.println("original : " + file.getOriginalFilename());
                System.out.println("request : " + image);
                break;
            }
        }
        assertThat(deleteFile).isNotNull();
        DeleteFileDto dto = new DeleteFileDto();
        dto.setFiles(new LinkedList<>());
        dto.getFiles().add(deleteFile);
        dataController.deleteDetailImages(ad.getId()+"", dto);

        String imageResourcePath = FileUtil.getImageResourcePath();
        Resource resource = loader.getResource(imageResourcePath + contentAd.getResourcePath());
        Path path = resource.getFile().toPath();
        File deletedFile = path.resolve(file.getOriginalFilename()).toFile();
        assertThat(deletedFile.exists()).isFalse();
    }
}