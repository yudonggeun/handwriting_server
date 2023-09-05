package com.promotion.handwriting.repository.file;

import com.promotion.handwriting.TestClass;
import com.promotion.handwriting.entity.Image;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

class FileRepositoryTest extends TestClass {
    @Autowired
    FileRepository repository;
    @Value("#{systemProperties['user.dir']}")
    private String resourcePath;
    @Value("${directory.image}")
    private String imagePath;

    @Test
    @DisplayName("이미지를 저장되고 삭제된다")
    void createTemperFile() throws IOException {
        // given
        File file = new File(resourcePath + "/src/test/resources/test.jpg");
        Image image = Image.builder()
                .imageName("original.jpg")
                .compressImageName("compress.jpg")
                .build();
        // when : 이미지를 저장하면 이미지 파일이 존재한다.
        repository.save(image, getMultipartFile(file));
        File origin = new File(resourcePath + imagePath + image.getImageUrl());
        File compress = new File(resourcePath + imagePath + image.getCompressImageUrl());
        // then
        assertThat(origin.exists()).isTrue();
        assertThat(compress.exists()).isTrue();

        // when : 이미지를 삭제하면 이미지 파일이 존재하지 않는다.
        repository.delete(image);
        // then
        assertThat(origin.exists()).isFalse();
        assertThat(compress.exists()).isFalse();
    }

    private MultipartFile getMultipartFile(File file) throws IOException {
        FileItem fileItem = new DiskFileItem("originFile", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

        InputStream input = new FileInputStream(file);
        OutputStream os = fileItem.getOutputStream();
        IOUtils.copy(input, os);

        MultipartFile mFile = new CommonsMultipartFile(fileItem);
        return mFile;
    }
}