package com.promotion.handwriting.repository.file;

import com.promotion.handwriting.WebConfig;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileRepository {

    private final WebConfig config;

    public void save(Image image, MultipartFile multipartFile) {
        try {
            save(image.getImagePath(), multipartFile.getInputStream());
            compressAndSave(image.getImageName(), image.getCompressImageName());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 저장 실패");
        }
    }

    public void delete(Image image) {
        try {
            delete(image.getImagePath());
            delete(image.getCompressImagePath());
        } catch (IOException ex) {
            throw new IllegalArgumentException("파일 삭제 실패");
        }
    }

    private void save(String filePath, InputStream data) throws IOException {
        File file = new File(config.getImageDirectory() + filePath);
        if (!file.createNewFile()) throw new IllegalArgumentException("토큰 식별자가 중복되었습니다.");
        FileCopyUtils.copy(data, Files.newOutputStream(file.toPath()));
    }

    private boolean compressAndSave(String originFilename, String targetFilename) throws IOException {
        String directoryPath = config.getImageDirectory();
        File originFile = new File(directoryPath + "/" + originFilename);
        if (!originFile.exists()) throw new IOException("원본파일이 존재하지 않습니다.");
        return ImageUtil.compress(originFile.getAbsolutePath(), directoryPath + "/" + targetFilename);
    }

    private void delete(String Path) throws IOException {
        new File(config.getImageDirectory() + Path).delete();
    }

}
