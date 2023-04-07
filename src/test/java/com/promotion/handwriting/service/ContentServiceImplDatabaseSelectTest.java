package com.promotion.handwriting.service;

import com.promotion.handwriting.dto.ContentResponse;
import com.promotion.handwriting.dto.image.ImageResponse;
import com.promotion.handwriting.new_handwriting.domain.Content;
import com.promotion.handwriting.new_handwriting.domain.type.ContentType;
import com.promotion.handwriting.new_handwriting.dto.SearchCondition;
import com.promotion.handwriting.new_handwriting.repository.ContentRepository;
import com.promotion.handwriting.new_handwriting.repository.ImageRepo;
import com.promotion.handwriting.new_handwriting.dto.request.CreateImageRequest;
import com.promotion.handwriting.new_handwriting.service.ContentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ContentServiceImplDatabaseSelectTest {

    @Autowired
    ContentService service;
    @Autowired
    ContentRepository repository;
    @Autowired
    ImageRepo imageRepository;

    Content content;

    @BeforeEach
    public void create_temporary_content_data() {

        for (int i = 0; i < 100; i++) {
            content = Content.builder(ContentType.CONTENT, "test_title" + i)
                    .build();
            content = repository.save(content);
        }
    }

    @Test
    @DisplayName("조회 테스트")
    public void test_select_content() {
        //given
        SearchCondition condition = new SearchCondition();
        PageRequest page = PageRequest.of(0, 10);
        //when
        Page<ContentResponse> selectResult = service.selectPage(page, condition);

        //then
        assertThat(selectResult.getSize()).isLessThanOrEqualTo(10);
        assertThat(selectResult.getContent()).doesNotContainNull();
    }

    @Test
    @DisplayName("이미지 조회")
    public void test_select_image_response() {
        //given
        int pageSize = 2;
        int pageNum = 0;
        PageRequest page = PageRequest.of(pageNum, pageSize);

        for (int i = 0; i < 10; i++) {
            CreateImageRequest request = new CreateImageRequest();
            request.setContentId(content.getId());
            request.setOriginName("request_filename" + i);
            request.setCompressName("request_compress_filename" + i);
            request.setPath("/test/path");

            service.createImage(request);
        }

        //when
        Page<ImageResponse> response = service.selectImageInformation(content.getId(), page);
        List<ImageResponse> imageList = imageRepository.findAllByContentId(content.getId()).stream()
                .map(i -> new ImageResponse(i.originPath(), i.compressPath())).collect(Collectors.toList());
        //then
        assertThat(response.getSize()).isLessThanOrEqualTo(pageSize);
        assertThat(imageList).containsAll(response.getContent());
        assertThat(imageList.size() / pageSize + (imageList.size() % pageSize != 0 ? 1 : 0)).isEqualTo(response.getTotalPages());
    }
}