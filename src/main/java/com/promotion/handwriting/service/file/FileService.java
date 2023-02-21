package com.promotion.handwriting.service.file;

import com.promotion.handwriting.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    Image saveImage(MultipartFile file, String resourcePath) throws IOException;

    boolean deleteImage(Image image, String resourcePath);
}
