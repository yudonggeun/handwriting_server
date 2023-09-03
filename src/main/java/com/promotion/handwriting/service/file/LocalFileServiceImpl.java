package com.promotion.handwriting.service.file;

import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Primary
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LocalFileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Override
    public Image saveImage(MultipartFile file, String resourcePath) throws IOException {
        String originFileName = file.getOriginalFilename();
        String compressFileName = "compress-" + originFileName;
        String originFilePath = resourcePath + "/" + originFileName;
        String targetFilePath = resourcePath + "/" + compressFileName;

        fileRepository.save(originFileName, resourcePath, file.getInputStream());
        fileRepository.compressAndSave(originFilePath, targetFilePath, resourcePath);

        return Image.builder()
                .imageName(originFileName)
                .compressImageName(compressFileName)
                .build();
    }

    @Override
    public boolean deleteImage(Image image, String resourcePath) {
        return fileRepository.delete(resourcePath, image.getImageName()) &&
                fileRepository.delete(resourcePath, image.getCompressImageName());
    }
}
