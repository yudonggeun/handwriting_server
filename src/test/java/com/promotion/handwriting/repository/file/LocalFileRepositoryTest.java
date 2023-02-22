package com.promotion.handwriting.repository.file;

import com.promotion.handwriting.dto.file.LocalFileToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class LocalFileRepositoryTest {

    @Autowired
    LocalFileRepository localFileRepository;
    String path = "/";

    @Test
    void compressFileTest(){

        File sample = new File();
        LocalFileToken.save()
        localFileRepository.save()
    }
}