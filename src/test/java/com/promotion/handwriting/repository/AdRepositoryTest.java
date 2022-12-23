package com.promotion.handwriting.repository;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Rollback
@Transactional
class AdRepositoryTest {

    @Autowired
    AdRepository adRepository;
    @Autowired
    ImageRepository imageRepository;

    Ad test_intro_ad = new Ad(AdType.INTRO, "", "소개입니다.", "/test_file", new LinkedList<>());
    Ad test_content1 = new Ad(AdType.CONTENT, "test Content1", "소1개입니다.", "/" + "test_file1", new LinkedList<>());
    Ad test_content2 = new Ad(AdType.CONTENT, "test Content2", "소2개입니다.", "/" + "test_file2", new LinkedList<>());
    Ad test_content3 = new Ad(AdType.CONTENT, "test Content3", "소34개입니다.", "/" + "test_file34", new LinkedList<>());
    Ad test_content4 = new Ad(AdType.CONTENT, "test Content4", "소4개입니다.", "/" + "test_fi4le", new LinkedList<>());
    Ad test_content5 = new Ad(AdType.CONTENT, "findAdWithImagesById", "소개입니다.", "/test_file", new LinkedList<>());

    Image image1 = new Image(test_content5, 0, "test1.png");
    Image image2 = new Image(test_content5, 1, "test2.png");
    Image image3 = new Image(test_content5, 3, "test3.png");

    AdRepositoryTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        log.info("set up");

        image1 = imageRepository.save(image1);
        image2 = imageRepository.save(image2);
        image3 = imageRepository.save(image3);

        test_content5.getImages().addAll(List.of(image1, image2, image3));

        test_intro_ad = adRepository.save(test_intro_ad);

        test_content1 = adRepository.save(test_content1);
        test_content2 = adRepository.save(test_content2);
        test_content3 = adRepository.save(test_content3);
        test_content4 = adRepository.save(test_content4);
        test_content5 = adRepository.save(test_content5);
    }

    @AfterEach
    void tearDown() {
        log.info("tear down");
        adRepository.delete(test_intro_ad);
        test_content5.setImages(null);
        imageRepository.deleteAll();
        adRepository.delete(test_content5);
        adRepository.deleteAllInBatch(List.of(test_content1, test_content2, test_content3, test_content4));
    }

    @Test
    void findByType() {
        Ad intro = adRepository.findIntroAd();
        assertThat(test_intro_ad.getDetail()).isEqualTo(intro.getDetail());
        assertThat(test_intro_ad.getType()).isEqualTo(intro.getType());
        assertThat(test_intro_ad.getTitle()).isEqualTo(intro.getTitle());
        assertThat(test_intro_ad.getResourcePath()).isEqualTo(intro.getResourcePath());
        assertThat(test_intro_ad.getImages().size()).isEqualTo(intro.getImages().size());
        List<Image> images = test_intro_ad.getImages();
        List<Image> introImages = intro.getImages();
        for (int i = 0; i < images.size(); i++) {
            assertThat(introImages.contains(images.get(i))).isTrue();
        }
    }

    @Test
    void findAlByType() throws IOException {
        List<Ad> contents = adRepository.findContents();
        assertThat(contents.size()).isEqualTo(5);
        level1:
        for (Ad content : contents) {
            assertThat(content.getType()).isEqualTo(AdType.CONTENT);
            for (Ad ad : List.of(test_content1, test_content2, test_content3, test_content4, test_content5)) {
                if (
                        content.getDetail().equals(ad.getDetail()) &&
                                content.getResourcePath().equals(ad.getResourcePath()) &&
                                content.getTitle().equals(ad.getTitle()) &&
                                content.getImages().size() == ad.getImages().size()
                ) {
                    continue level1;
                }
            }

            log.info("데이터 정합성 테스트 실패");
            assertThat(true).isFalse();
        }

    }

    @Test
    void findAdWithImagesById() throws IOException {

        Ad introAd = adRepository.findIntroAd();
        Ad targetAd = adRepository.findAdWithImagesById(introAd.getId());

        assertThat(test_intro_ad.getDetail()).isEqualTo(targetAd.getDetail());
        assertThat(test_intro_ad.getType()).isEqualTo(targetAd.getType());
        assertThat(test_intro_ad.getTitle()).isEqualTo(targetAd.getTitle());
        assertThat(test_intro_ad.getResourcePath()).isEqualTo(targetAd.getResourcePath());
        assertThat(test_intro_ad.getImages().size()).isEqualTo(targetAd.getImages().size());

        List<Image> targetAdImages = targetAd.getImages();
        List<Image> introImages = test_intro_ad.getImages();
        for (int i = 0; i < introImages.size(); i++) {
            assertThat(targetAdImages.contains(introImages.get(i))).isTrue();
        }
    }
}