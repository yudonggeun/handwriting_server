package com.promotion.handwriting.service;

import com.promotion.handwriting.new_handwriting.domain.Content;
import com.promotion.handwriting.new_handwriting.domain.Nimage;
import com.promotion.handwriting.new_handwriting.domain.type.ContentType;
import com.promotion.handwriting.new_handwriting.dto.ContentCreateDto;
import com.promotion.handwriting.new_handwriting.repository.ContentRepository;
import com.promotion.handwriting.new_handwriting.repository.ImageRepo;
import com.promotion.handwriting.new_handwriting.request.CreateImageRequest;
import com.promotion.handwriting.new_handwriting.request.UpdateContentRequest;
import com.promotion.handwriting.new_handwriting.service.ContentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ContentServiceImplTest {

    @Autowired
    ContentService service;
    @Autowired
    ContentRepository repository;
    @Autowired
    ImageRepo imageRepository;

    Content content;
    @BeforeEach
    public void create_temporary_content_data(){
        content = Content.builder("test_id" + UUID.randomUUID(), "test_title")
                .build();
        content = repository.save(content);
    }
    @Test
    @DisplayName("컨텐츠 생성하기")
    public void test_create_content() {
        //given
        ContentCreateDto dto = new ContentCreateDto();
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
        String contentId = content.getId();

        //when
        service.deleteContent(contentId);

        //then
        Optional<Content> optionalContent = repository.findById(contentId);

        assertThat(optionalContent.isEmpty()).isTrue();

    }

    @Test
    @DisplayName("이미지 database에 생성하기")
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
    @DisplayName("이미지 데이터 베이스에서 삭제 하기")
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