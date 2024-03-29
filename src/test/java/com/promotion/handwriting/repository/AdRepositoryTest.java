package com.promotion.handwriting.repository;

import com.promotion.handwriting.TestClass;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.database.AdRepository;
import com.promotion.handwriting.repository.database.JpaImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Slf4j
class AdRepositoryTest extends TestClass {

    @Autowired
    AdRepository adRepository;
    @Autowired
    JpaImageRepository imageRepository;

    @DisplayName("특정 id를 가진 content는 삭제되고 하위 이미지도 같이 삭제되어야한다.")
    @Test
    public void deleteById() {
        // given
        Ad content = saveContent(AdType.CONTENT, "", "");
        saveImage(content, "deleteById.jpg");
        // when
        imageRepository.deleteAllInBatch();
        adRepository.deleteById(content.getId());
        // then
        assertThat(adRepository.findById(content.getId())).isNull();
        assertThat(imageRepository.findByAdId(content.getId(), PageRequest.of(0, 1))).isEmpty();
    }

    @DisplayName("Content 타입의 컨텐츠는 모두 content 타입이다.")
    @Test
    public void findByType() {
        // given when
        saveContent(AdType.CONTENT, "test Content1", "소1개입니다.");
        saveContent(AdType.CONTENT, "test Content1", "소1개입니다.");
        // then
        assertThat(adRepository.findByType(AdType.CONTENT, PageRequest.of(0, 2))).hasSize(2)
                .extracting("type", "title", "detail")
                .contains(tuple(AdType.CONTENT, "test Content1", "소1개입니다."));
    }

    @DisplayName("특정 id의 컨텐츠를 조회시 id 값이 같고 관련된 image를 조회할 수 있다.")
    @Test
    public void findById() {
        // given
        Ad content = saveContent(AdType.CONTENT, "", "");
        saveImage(content, "image.jpg");
        // when
        var findContent = adRepository.findWithImageById(content.getId());
        // then
        assertThat(content.getId()).isEqualTo(findContent.getId());
        assertThat(findContent.getImages()).hasSize(1);
    }

    private Image saveImage(Ad content, String fileName) {
        return imageRepository.saveAndFlush(Image.builder()
                .content(content)
                .imageName(fileName)
                .priority(1)
                .build());
    }

    private Ad saveContent(AdType adType, String title, String detail) {
        return adRepository.saveAndFlush(Ad.builder()
                .type(adType)
                .title(title)
                .detail(detail)
                .build());
    }

}