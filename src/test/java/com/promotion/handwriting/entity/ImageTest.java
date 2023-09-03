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
        Image image = Image.builder().imageName("myImage.jpg").build();
        // when then
        assertThat(image.getImageName()).isEqualTo("myImage.jpg");
    }

    @DisplayName("압축 이미지 이름을 반환한다.")
    @Test
    public void getZipImageName() {
        // given
        Image image = Image.builder().compressImageName("myImage.jpg").build();
        // when then
        assertThat(image.getZipImageName()).isEqualTo("myImage.jpg");
    }

    @DisplayName("주어진 imageUrl 에 알맞는 이미지 엑세스 url 토큰을 생성한다.")
    @Nested
    class urlDto{
        Ad content = Ad.builder().resourcePath("/content").build();
        @DisplayName("image 저장 디렉토리 경로가 '/' 일 때")
        @Test
        void root(){
            // given
            Image image = Image.builder().content(content).compressImageName("compress.jpg").imageName("original.jpg").build();
            // when
            ImageUrlDto urlDto = image.urlDto("/");
            // then
            assertThat(urlDto).extracting("original", "compress")
                    .containsExactly("/content/original.jpg", "/content/compress.jpg");
        }

        @DisplayName("image 저장 디렉토리 경로가 '/'가 아닐 때")
        @Test
        void directory(){
            // given
            Image image = Image.builder().content(content).compressImageName("compress.jpg").imageName("original.jpg").build();
            // when
            ImageUrlDto urlDto = image.urlDto("/root");
            // then
            assertThat(urlDto).extracting("original", "compress")
                    .containsExactly("/root/content/original.jpg", "/root/content/compress.jpg");
        }
    }
}