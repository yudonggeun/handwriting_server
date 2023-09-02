package com.promotion.handwriting.service.file;

import com.promotion.handwriting.dto.file.FileToken;
import com.promotion.handwriting.dto.file.LocalFileToken;
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
        FileToken token = new LocalFileToken(file.getInputStream(), resourcePath, originFileName);

        fileRepository.save(token);
        fileRepository.compressAndSave(originFilePath, targetFilePath, resourcePath);

        return Image.builder().imageName(token.getFileName())
                .compressImageName(compressFileName)
                .build();
    }

    @Override
    public boolean deleteImage(Image image, String resourcePath) {
        FileToken token1 = new LocalFileToken(null, resourcePath, image.getImageName());
        FileToken token2 = new LocalFileToken(null, resourcePath, image.getImageName());

        return fileRepository.delete(token1) && fileRepository.delete(token2);
    }
}
