package com.promotion.handwriting.repository;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class ImageRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    AdRepository adRepository;

    @Autowired
    ImageRepository imageRepository;

    Ad ad = new Ad(AdType.CONTENT, "findAdWithImagesById", "소개입니다.", "/test_file", new LinkedList<>());

    Image image1 = new Image(ad, 0, "test1.png");
    Image image2 = new Image(ad, 1, "test2.png");
    Image image3 = new Image(ad, 3, "test3.png");

    ImageRepositoryTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        image1 = imageRepository.save(image1);
        image2 = imageRepository.save(image2);
        image3 = imageRepository.save(image3);

        ad.getImages().addAll(List.of(image1, image2, image3));
        ad = adRepository.save(ad);
        em.flush();
        em.clear();
    }

    @AfterEach
    void tearDown() {
        imageRepository.deleteAll();
        adRepository.deleteAll();
    }

    @Test
    void findByAdId() {
        List<Image> findImages = imageRepository.findByAd(ad);

        List<Image> images = ad.getImages();
        find : for (Image findImage : findImages) {
            for (Image image : images) {
                if (
                        image.getId() == findImage.getId() &&
                        image.getImageName().equals(findImage.getImageName()) &&
                        image.getPriority() == findImage.getPriority()
                ) {
                    continue find;
                }
            }
            assertThat(false).isTrue();
        }
    }

    @Test
    void deleteByImageNameAndAd() {
        imageRepository.deleteByImageNameAndAd(image1.getImageName(), ad);
        Ad findAd = adRepository.findAdWithImagesById(ad.getId());
        assertThat(findAd.getImages().contains(image1)).isFalse();
    }

    @Test
    void deleteAllByAd() {
        imageRepository.deleteAllByAd(ad);
        Ad find = adRepository.findAdWithImagesById(ad.getId());
        assertThat(find.getImages().size()).isZero();
    }
}