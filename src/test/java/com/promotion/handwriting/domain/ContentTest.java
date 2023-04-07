package com.promotion.handwriting.domain;

import com.promotion.handwriting.new_handwriting.domain.Content;
import com.promotion.handwriting.new_handwriting.domain.Nimage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.promotion.handwriting.new_handwriting.domain.type.ContentType.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ContentTest {

    @Test
    public void createContent() {
        Content content = Content.builder(CONTENT, "title1")
                .build();

        assertThat(content).isNotNull();
    }

    @Test
    @DisplayName("image list 방어적 복사 확인")
    public void contentGetMethod() {
        Content content = Content.builder(CONTENT, "test_title")
                .description("test_description")
                .images(new ArrayList<>())
                .build();

        List<Nimage> images = content.getImages();
        images.add(new Nimage("test", "test", "test", content.getId()));

        assertThat(content).isNotNull();
        assertThat(content.getDescription()).isEqualTo("test_description");
        assertThat(content.getTitle()).isEqualTo("test_title");
        assertThat(content.getImages().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("content 수정 메서드 확인")
    public void test_changeText() {
        //given
        Content content = Content.builder(CONTENT, "before")
                .build();
        String after_title = "after_title";
        String after_description = "after_description";
        //when
        content.changeTitleText(after_title);
        content.changeDescriptionText(after_description);
        //then
        assertThat(content.getTitle()).isEqualTo(after_title);
        assertThat(content.getDescription()).isEqualTo(after_description);
    }

}