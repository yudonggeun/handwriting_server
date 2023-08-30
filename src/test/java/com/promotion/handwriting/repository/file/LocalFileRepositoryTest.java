package com.promotion.handwriting.repository.file;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class LocalFileRepositoryTest {

    @Autowired
    LocalFileRepository localFileRepository;
    String path = "/";

    @Test
    void compressFileTest(){

    }
}