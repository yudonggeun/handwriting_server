package com.promotion.handwriting.service.file;

import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface AwsService {
    void upload(File file, String key);

    void upload(InputStream inputStream, String key, String contentType, long contentLength);

    void upload(PutObjectRequest request);

    void uploadFiles(List<File> files, String key);

    void copy(String orgKey, String copyKey);

    void delete(String key);
}
