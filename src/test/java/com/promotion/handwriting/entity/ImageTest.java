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
        Image image = createImage("myImage.jpg", null);
        // when then
        assertThat(image.getImagePath()).matches("/.*myImage.jpg");
    }

    @DisplayName("압축 이미지는 zip-{imageName}이다.")
    @Test
    public void getZipImageName() {
        // given
        var image = createImage("data.jpg", null);
        // when then
        assertThat(image.getCompressImagePath()).matches("/zip-.*data.jpg");
    }

    @DisplayName("주어진 imageUrl 에 알맞는 이미지 엑세스 url 토큰을 생성한다.")
    @Test
    void root() {
        // given
        Ad content = Ad.builder().build();
        var image = createImage("original.jpg", content);
        // when
        ImageUrlDto urlDto = image.urlDto("");
        // then
        assertThat(urlDto.getOriginal()).matches("/.*original.jpg");
        assertThat(urlDto.getCompress()).matches("/zip.*original.jpg");
    }

    private Image createImage(String imageName, Ad content) {
        return Image.builder()
                .imageName(imageName)
                .content(content)
                .build();
    }
}