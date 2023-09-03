package com.promotion.handwriting.entity;

import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.file.FileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AdTest {

    @DisplayName("제목을 변경한다.")
    @Test
    void changeTitle() {
        // given
        var content = Ad.builder().title("before").build();
        // when
        content.setTitle("after");
        // then
        assertThat(content.getTitle()).isEqualTo("after");
    }

    @DisplayName("상세를 변경한다.")
    @Test
    void changeDescription() {
        // given
        var content = Ad.builder().detail("before").build();
        // when
        content.setDetail("after");
        // then
        assertThat(content.getDetail()).isEqualTo("after");
    }

    @DisplayName("도메인 객체 정보를 담은 데이터 객체를 생성한다.")
    @Test
    void createDto() throws IOException {
        // given
        var fileRepository = mock(FileRepository.class);
        var file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("file.jpg");

        var content = Ad.builder().type(AdType.CONTENT)
                .resourcePath("/path")
                .detail("detail")
                .title("title")
                .build();
        content.addImage(file, fileRepository);

        // when
        var dto = content.contentDto("/root");
        // then
        assertThat(dto).extracting("title", "description")
                .containsExactly("title", "detail");
        assertThat(dto.getImages().get(0).getOriginal())
                .hasToString("/root/path/file.jpg");
    }

    @DisplayName("이미지를 추가한다.")
    @Test
    void addImage() throws IOException {
        // given
        var fileRepository = mock(FileRepository.class);
        var file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("file.jpg");

        var content = Ad.builder().type(AdType.CONTENT)
                .resourcePath("/path")
                .detail("detail")
                .title("title")
                .build();
        // when
        content.addImage(file, fileRepository);
        // then
        Image image = content.getImages().get(0);
        assertThat(image.getImageName()).isEqualTo("file.jpg");
    }
}