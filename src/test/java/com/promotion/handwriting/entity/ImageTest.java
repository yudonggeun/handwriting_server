package com.promotion.handwriting.entity;

import com.promotion.handwriting.dto.ImageUrlDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ImageTest {


    @DisplayName("원본 이미지 이름을 반환한다.")
    @Test
    public void getImageName() {
        // given
        Image image = createImage("myImage.jpg", null, null);
        // when then
        assertThat(image.getImageUrl()).isEqualTo("/myImage.jpg");
    }

    @DisplayName("압축 이미지 이름을 반환한다.")
    @Test
    public void getZipImageName() {
        // given
        var image = createImage("data.jpg", "myImage.jpg", null);
        // when then
        assertThat(image.getCompressImageUrl()).isEqualTo("/myImage.jpg");
    }

    @DisplayName("주어진 imageUrl 에 알맞는 이미지 엑세스 url 토큰을 생성한다.")
    @Nested
    class urlDto {

        Ad content = Ad.builder().build();

        @DisplayName("image 저장 디렉토리 경로가 '/' 일 때")
        @Test
        void root() {
            // given
            var image = createImage("original.jpg", "compress.jpg", content);
            // when
            ImageUrlDto urlDto = image.urlDto();
            // then
            assertThat(urlDto).extracting("original", "compress")
                    .containsExactly("/original.jpg", "/compress.jpg");
        }

        @DisplayName("image 저장 디렉토리 경로가 '/'가 아닐 때")
        @Test
        void directory() {
            // given
            var image = createImage("original.jpg", "compress.jpg", content);
            // when
            ImageUrlDto urlDto = image.urlDto();
            // then
            assertThat(urlDto).extracting("original", "compress")
                    .containsExactly("/original.jpg", "/compress.jpg");
        }

    }

    private Image createImage(String imageName, String compressImageName, Ad content) {
        return Image.builder()
                .imageName(imageName)
                .compressImageName(compressImageName)
                .content(content)
                .build();
    }
}