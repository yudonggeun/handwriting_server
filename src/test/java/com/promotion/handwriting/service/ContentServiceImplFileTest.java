package com.promotion.handwriting.service;

import com.promotion.handwriting.new_handwriting.domain.Content;
import com.promotion.handwriting.new_handwriting.domain.Nimage;
import com.promotion.handwriting.new_handwriting.domain.type.ContentType;
import com.promotion.handwriting.new_handwriting.repository.ContentRepository;
import com.promotion.handwriting.new_handwriting.repository.FileRepo;
import com.promotion.handwriting.new_handwriting.repository.ImageRepo;
import com.promotion.handwriting.new_handwriting.dto.request.CreateImageRequest;
import com.promotion.handwriting.new_handwriting.service.ContentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ContentServiceImplFileTest {

    @Autowired
    ContentService service;
    @Autowired
    ContentRepository repository;
    @Autowired
    ImageRepo imageRepository;
    @Autowired
    FileRepo fileRepo;

    @Test
    @DisplayName("multipart image file을 서버에 저장하기")
    void test_multipart_image_file_save() throws IOException {
        Content content = Content.builder(ContentType.CONTENT, "test_title").build();
        repository.save(content);

        File file = new File("");
        String path = "/testPath";
        String originFileName = "test_image.jpg";
        String compressFileName = "test_compress_image.jpg";
        MockMultipartFile multipartFile = new MockMultipartFile(
            originFileName, new FileInputStream(file)
        );
        fileRepo.createDirectory(path);

        CreateImageRequest request = new CreateImageRequest();
        request.setContentId(content.getId());
        request.setPath(path);
        request.setCompressName(compressFileName);
        request.setOriginName(originFileName);
        Long imageId = service.createImage(request);

        Nimage image = imageRepository.findById(imageId).get();

        //when
        String originPath = image.originPath();
        String compressPath = image.compressPath();

        //then
        assertThat(originPath).isEqualTo(path + "/" + originFileName);
        assertThat(compressPath).isEqualTo(path + "/" + compressFileName);
    }
}