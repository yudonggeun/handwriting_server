package com.promotion.handwriting.repository;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.database.AdRepository;
import com.promotion.handwriting.repository.database.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Slf4j
class ImageRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    AdRepository adRepository;

    @Autowired
    ImageRepository imageRepository;

    Ad ad = createAd(AdType.CONTENT, "findAdWithImagesById", "소개입니다.", "/test_file");

    private Ad createAd(AdType type, String title, String detail, String resourcePath) {
        return Ad.builder()
                .type(type)
                .title(title)
                .detail(detail)
                .resourcePath(resourcePath)
                .build();
    }
    Image image1 = Image.builder().priority(0).imageName("test1.png").build();
    Image image2 = Image.builder().priority(1).imageName("test2.png").build();
    Image image3 = Image.builder().priority(3).imageName("test3.png").build();

    ImageRepositoryTest() throws IOException {
    }

    @BeforeEach
    void setUp() {
        image1 = imageRepository.save(image1);
        image2 = imageRepository.save(image2);
        image3 = imageRepository.save(image3);

        ad.addImages(List.of(image1, image2, image3));
        ad = adRepository.save(ad);
        em.flush();
    }

    @AfterEach
    void tearDown() {
        imageRepository.deleteAll();
        adRepository.deleteAll();
    }

    @Test
    void deleteAllByAd() {
        imageRepository.deleteAllByAd(ad.getId());
        Ad find = adRepository.findAdWithImagesById(ad.getId());
        assertThat(find.getImages().size()).isZero();
    }
}