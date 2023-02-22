package com.promotion.handwriting.repository;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.database.AdRepository;
import com.promotion.handwriting.repository.database.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    Ad test_intro_ad = Ad.createAd(AdType.INTRO, "", "소개입니다.", "/test_file");
    Ad test_content1 = Ad.createAd(AdType.CONTENT, "test Content1", "소1개입니다.", "/" + "test_file1");
    Ad test_content2 = Ad.createAd(AdType.CONTENT, "test Content2", "소2개입니다.", "/" + "test_file2");
    Ad test_content3 = Ad.createAd(AdType.CONTENT, "test Content3", "소34개입니다.", "/" + "test_file34");
    Ad test_content4 = Ad.createAd(AdType.CONTENT, "test Content4", "소4개입니다.", "/" + "test_fi4le");
    Ad test_content5 = Ad.createAd(AdType.CONTENT, "findAdWithImagesById", "소개입니다.", "/test_file");

    Image image1 = Image.builder().priority(0).imageName("test1.png").build();
    Image image2 = Image.builder().priority(1).imageName("test2.png").build();
    Image image3 = Image.builder().priority(3).imageName("test3.png").build();

    AdRepositoryTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        log.info("set up");

        test_content5.addImages(List.of(image1, image2, image3));

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
        adRepository.delete(test_content1);
        adRepository.delete(test_content2);
        adRepository.delete(test_content3);
        adRepository.delete(test_content4);
        adRepository.delete(test_content5);
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
        for (Image image : images) {
            assertThat(introImages.contains(image)).isTrue();
        }
    }

    @Test
    void findAlByType() {
        List<Ad> contents = adRepository.findContents();

        for (Ad content : contents) {
            assertThat(content.getType()).isEqualTo(AdType.CONTENT);
        }

        level1:
        for (Ad ad : List.of(test_content1, test_content2, test_content3, test_content4, test_content5)) {
            for (Ad content : contents) {
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
    void findAdWithImagesById() {

        Ad introAd = adRepository.findIntroAd();
        Ad targetAd = adRepository.findAdWithImagesById(introAd.getId());

        assertThat(test_intro_ad.getDetail()).isEqualTo(targetAd.getDetail());
        assertThat(test_intro_ad.getType()).isEqualTo(targetAd.getType());
        assertThat(test_intro_ad.getTitle()).isEqualTo(targetAd.getTitle());
        assertThat(test_intro_ad.getResourcePath()).isEqualTo(targetAd.getResourcePath());
        assertThat(test_intro_ad.getImages().size()).isEqualTo(targetAd.getImages().size());

        List<String> targetAdImages = targetAd.getImages().stream().map(image -> image.getImageName() + image.getPriority() + image.getId()).collect(Collectors.toList());
        List<String> introImages = test_intro_ad.getImages().stream().map(image -> image.getImageName() + image.getPriority() + image.getId()).collect(Collectors.toList());

        for (String introImage : introImages) {
            assertThat(targetAdImages.contains(introImage)).isTrue();
        }
    }
}