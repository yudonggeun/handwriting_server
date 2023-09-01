package com.promotion.handwriting.repository;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.database.AdRepository;
import com.promotion.handwriting.repository.database.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Slf4j
class ImageRepositoryTest {
    @Autowired
    AdRepository adRepository;
    @Autowired
    ImageRepository imageRepository;

    @DisplayName("컨텐츠에 속한 이미지가 전부 삭제된다.")
    @Test
    void deleteAllByAd() {
        // given
        var content = adRepository.save(createAd(AdType.CONTENT, "findAdWithImagesById", "소개입니다.", "/test_file"));
        // when
        imageRepository.deleteAllByAdId(content.getId());
        // then
        assertThat(imageRepository.findByAdId(content.getId())).isEmpty();
    }

    @DisplayName("특정 컨텐츠에 이미지 데이터를 저장하면 컨텐츠 id로 이미지 조회시 저장된 이미지가 조회된다.")
    @Test
    void findByAd() {
        // given
        var content = adRepository.save(createAd(AdType.CONTENT, "findAdWithImagesById", "소개입니다.", "/test_file"));
        Image image = imageRepository.save(Image.builder()
                .content(content)
                .compressImageName("test")
                .imageName("test")
                .priority(1)
                .build());
        // when
        List<Image> images = imageRepository.findByAdId(content.getId());
        // then
        assertThat(images).map(e -> e.getId()).contains(image.getId());
    }

    @AfterEach
    void tearDown() {
        imageRepository.deleteAllInBatch();
        adRepository.deleteAllInBatch();
    }

    private Ad createAd(AdType type, String title, String detail, String resourcePath) {
        return Ad.builder()
                .type(type)
                .title(title)
                .detail(detail)
                .resourcePath(resourcePath)
                .build();
    }
}