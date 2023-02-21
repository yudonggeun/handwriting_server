package com.promotion.handwriting.repository.file;

import com.promotion.handwriting.dto.parent.FileToken;
import com.promotion.handwriting.util.FileUtil;
import com.promotion.handwriting.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Primary
@Service
@RequiredArgsConstructor
@Slf4j
public class LocalFileRepository implements FileRepository {

    private final ResourceLoader loader;

    @Override
    public boolean save(FileToken token) {
        try {
            Resource resource = loader.getResource(FileUtil.getImageResourcePath() + token.getDirectory());
            Path directory = resource.getFile().toPath();
            Path location = directory.resolve(token.getFileName());
            if (!location.toFile().createNewFile())
                throw new IllegalArgumentException("토큰 식별자가 중복되었습니다.");

            FileCopyUtils.copy(token.getInputStream(), Files.newOutputStream(location));
            return true;
        } catch (IOException ex) {
            log.error("파일 저장 실패 : " + token.getDirectory() + "/" + token.getFileName());
            return false;
        } catch (IllegalArgumentException ex) {
            log.error("토큰 식별자가 중복되었습니다.");
            return false;
        }
    }

    @Override
    public boolean compressAndSave(String originFilename, String targetFilename, String resourcePath) throws IOException {
        Resource resource = loader.getResource(FileUtil.getImageResourcePath() + resourcePath);
        String directoryPath = resource.getFile().getAbsolutePath();

        File originFile = new File(directoryPath + "/" + originFilename);
        if (!originFile.exists()) return false;
        String compressFileName = ImageUtil.compressImageName(originFilename);
        return ImageUtil.compress(originFile.getAbsolutePath(), directoryPath + "/" + compressFileName);
    }

    @Override
    public boolean delete(FileToken token) {
        try {
            Resource resource = loader.getResource(FileUtil.getImageResourcePath() + token.getDirectory());
            Path directory = resource.getFile().toPath();
            File file = new File(directory.toAbsolutePath() + "/" + token.getFileName());
            if (!file.exists()) {
                log.error("파일 삭제 실패 : 파일이 존재하지 않습니다.");
            } else if (!file.isFile()) {
                log.error("파일 삭제 실패 : 파일이 아닙니다.");
            }
            return file.delete();
        } catch (IOException ex) {
            log.error("파일 삭제 실패 : " + token.getDirectory() + "/" + token.getFileName());
            return false;
        }
    }

    @Override
    public boolean deleteDirectory(FileToken token) {
        try {
            Resource resource = loader.getResource(FileUtil.getImageResourcePath() + token.getDirectory());
            if (resource.getFile().isDirectory()) {//디렉터리 삭제
                FileUtil.deleteDirectory(resource.getFile());
            }
            return true;
        } catch (IOException ex) {
            log.error("디렉터리 삭제 실패 : " + token.getDirectory() + "/" + token.getFileName());
            return false;
        }
    }
}
