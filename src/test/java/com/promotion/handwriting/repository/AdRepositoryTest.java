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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Rollback
class AdRepositoryTest {

    @Autowired
    AdRepository adRepository;
    @Autowired
    ImageRepository imageRepository;

    @DisplayName("특정 id를 가진 content는 삭제되고 하위 이미지도 같이 삭제되어야한다.")
    @Test
    public void deleteById() {
        // given
        Ad content = adRepository.save(createAd(AdType.CONTENT, "", "", "/"));
        imageRepository.saveAndFlush(Image.builder().content(content).build());
        // when
        adRepository.deleteById(content.getId());
        // then
        assertThat(adRepository.findById(content.getId())).isEmpty();
        assertThat(imageRepository.findByAdId(content.getId())).isEmpty();
    }

    @DisplayName("Content 타입의 컨텐츠는 모두 content 타입이다.")
    @Test
    public void findByType() {
        // given when
        adRepository.save(createAd(AdType.CONTENT, "test Content1", "소1개입니다.", "/" + "test_file1"));
        adRepository.save(createAd(AdType.CONTENT, "test Content1", "소1개입니다.", "/" + "test_file1"));
        adRepository.save(createAd(AdType.INTRO, "test intro", "intro소1개입니다.", "/" + "test_file1"));
        // then
        assertThat(adRepository.findByType(AdType.CONTENT, PageRequest.of(0, 2))).hasSize(2)
                .extracting("type", "title", "detail", "resourcePath")
                .contains(tuple(AdType.CONTENT, "test Content1", "소1개입니다.", "/" + "test_file1"));
        assertThat(adRepository.findByType(AdType.INTRO, PageRequest.of(0, 1))).hasSize(1)
                .extracting("type", "title", "detail", "resourcePath")
                .contains(tuple(AdType.INTRO, "test intro", "intro소1개입니다.", "/" + "test_file1"));
    }

    @DisplayName("특정 id의 컨텐츠를 조회시 id 값이 같고 관련된 image를 조회할 수 있다.")
    @Test
    public void findById() {
        // given
        Ad content = adRepository.saveAndFlush(createAd(AdType.CONTENT, "", "", "/"));
        imageRepository.saveAndFlush(Image.builder().content(content).build());
        // when
        var findContent = adRepository.findWithImageById(content.getId());
        // then
        assertThat(content.getId()).isEqualTo(findContent.getId());
        assertThat(findContent.getImages()).hasSize(1);
    }
    private Ad createAd(AdType type, String title, String detail, String resourcePath) {
        return Ad.builder()
                .type(type)
                .title(title)
                .detail(detail)
                .resourcePath(resourcePath)
                .build();
    }

    @AfterEach
    void tearDown() {
        imageRepository.deleteAllInBatch();
        adRepository.deleteAllInBatch();
    }
}