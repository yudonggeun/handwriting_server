package com.promotion.handwriting.service;

import com.promotion.handwriting.new_handwriting.domain.Content;
import com.promotion.handwriting.new_handwriting.domain.Nimage;
import com.promotion.handwriting.new_handwriting.domain.type.ContentType;
import com.promotion.handwriting.new_handwriting.dto.CreateContentRequest;
import com.promotion.handwriting.new_handwriting.repository.ContentRepository;
import com.promotion.handwriting.new_handwriting.repository.ImageRepo;
import com.promotion.handwriting.new_handwriting.dto.request.CreateImageRequest;
import com.promotion.handwriting.new_handwriting.dto.request.UpdateContentRequest;
import com.promotion.handwriting.new_handwriting.service.ContentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ContentServiceImplDatabaseTest {

    @Autowired
    ContentService service;
    @Autowired
    ContentRepository repository;
    @Autowired
    ImageRepo imageRepository;

    Content content;
    @BeforeEach
    public void create_temporary_content_data(){
        content = Content.builder(ContentType.CONTENT, "test_title")
                .build();
        content = repository.save(content);
    }
    @Test
    @DisplayName("컨텐츠 생성하기")
    public void test_create_content() {
        //given
        CreateContentRequest dto = new CreateContentRequest();
        String title = "test_title";
        String description = "test_description";
        ContentType type = ContentType.CONTENT;
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setType(type);

        //when
        String contentId = service.createContent(dto);
        Optional<Content> target = repository.findById(contentId);

        //then
        assertThat(target.isEmpty()).isFalse();
        assertThat(target.get().getId()).isEqualTo(contentId);
        assertThat(target.get().getTitle()).isEqualTo(title);
        assertThat(target.get().getDescription()).isEqualTo(description);
    }

    @Test
    @DisplayName("컨텐츠 수정하기")
    public void test_update_service_test() {
        //given

        //when
        String afterTitle = "after_title";
        String afterDescription = "after_description";

        UpdateContentRequest request = new UpdateContentRequest();
        request.setContentId(content.getId());
        request.setTitle(afterTitle);
        request.setDescription(afterDescription);
        service.updateContent(request);

        //then
        Content findContent = repository.findById(content.getId()).orElseThrow();
        assertThat(findContent.getTitle()).isEqualTo(afterTitle);
        assertThat(findContent.getDescription()).isEqualTo(afterDescription);
    }

    @Test
    @DisplayName("이미지 없는 컨텐츠 삭제하기")
    public void test_delete_content_row(){
        //given

        //when
        service.deleteContent(content.getId());

        //then
        Optional<Content> optionalContent = repository.findById(content.getId());

        assertThat(optionalContent.isEmpty()).isTrue();

    }

    @Test
    @DisplayName("이미지 있는 컨텐츠 삭제하기")
    public void test_delete_content_with_image(){
        //given
        CreateImageRequest request = new CreateImageRequest();
        request.setContentId(content.getId());
        request.setOriginName("request_filename");
        request.setCompressName("request_compress_filename");
        request.setPath("/test/path");

        Long imageId = service.createImage(request);

        //when
        service.deleteContent(content.getId());
        Optional<Content> optionalContent = repository.findById(content.getId());
        Optional<Nimage> optionalImage = imageRepository.findById(imageId);
        //then
        assertThat(optionalContent.isEmpty()).isTrue();
        assertThat(optionalImage.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("이미지 생성하기")
    public void test_create_image_in_db() {
        //given
        CreateImageRequest request = new CreateImageRequest();
        request.setContentId(content.getId());
        request.setOriginName("request_filename");
        request.setCompressName("request_compress_filename");
        request.setPath("/test/path");

        //when
        Long imageId = service.createImage(request);
        Nimage image = imageRepository.findById(imageId).orElseThrow();

        //then
        assertThat(image).isEqualTo(new Nimage(request.getOriginName(), request.getCompressName(), request.getPath(), request.getContentId()));
    }

    @Test
    @DisplayName("이미지 삭제 하기")
    public void test_delete_image_in_db() {
        //given
        Nimage image = new Nimage("test", "testcompress", "/path", content.getId());
        repository.save(content);
        imageRepository.save(image);
        //when
        service.deleteImage(image);
        //then
        Optional<Nimage> optionalImage = imageRepository.findById(image.getId());
        assertThat(optionalImage.isEmpty()).isTrue();
    }

}