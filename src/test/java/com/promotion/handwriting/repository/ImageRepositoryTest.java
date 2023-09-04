package com.promotion.handwriting.repository;

import com.promotion.handwriting.TestClass;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Slf4j
class ImageRepositoryTest extends TestClass {
    @Autowired
    AdRepository adRepository;
    @Autowired
    ImageRepository imageRepository;

    @DisplayName("컨텐츠에 속한 이미지가 전부 삭제된다.")
    @Test
    void deleteAllByAd() {
        // given
        var content = saveContent(AdType.CONTENT, "findAdWithImagesById", "소개입니다.", "/test_file");
        // when
        imageRepository.deleteAllByAdId(content.getId());
        // then
        assertThat(imageRepository.findByAdId(content.getId(), PageRequest.of(0, 1))).isEmpty();
    }

    @DisplayName("특정 컨텐츠에 이미지 데이터를 저장하면 컨텐츠 id로 이미지 조회시 저장된 이미지가 조회된다.")
    @Test
    void findByAd() {
        // given
        var content = saveContent(AdType.CONTENT, "findAdWithImagesById", "소개입니다.", "/test_file");
        Image image = saveImage(content, "test", "test");
        // when
        List<Image> images = imageRepository.findByAdId(content.getId(), PageRequest.of(0, 1)).getContent();
        // then
        assertThat(images).map(e -> e.getId()).contains(image.getId());
    }

    @DisplayName("컨텐츠 id가 같고 이미지 id가 같은 모든 이미지 데이터를 삭제한다.")
    @Test
    public void deleteAllByAdIdAndIdIn() {
        //given
        Ad content = saveContent(AdType.CONTENT, "title", "", "/");
        Image deleteImage = saveImage(content, "zip.jpg", "data.jpg");
        Image image = saveImage(content, "zip.jpg", "data.jpg");
        //when
        imageRepository.deleteAllByAdIdAndIdIn(content.getId(), List.of(deleteImage.getId()));
        Page<Image> images = imageRepository.findByAdId(content.getId(), PageRequest.of(0, 10));
        //then
        assertThat(images).hasSize(1);
    }

    @AfterEach
    void tearDown() {
        imageRepository.deleteAllInBatch();
        adRepository.deleteAllInBatch();
    }


    private Ad saveContent(AdType type, String title, String detail, String path) {
        return adRepository.saveAndFlush(Ad.builder()
                .type(type)
                .title(title)
                .detail(detail)
                .resourcePath(path)
                .build());
    }

    private Image saveImage(Ad content, String zipFileName, String fileName) {
        return imageRepository.saveAndFlush(Image.builder()
                .content(content)
                .imageName(fileName)
                .compressImageName(zipFileName)
                .priority(1)
                .build());
    }
}