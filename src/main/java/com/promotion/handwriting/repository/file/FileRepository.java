package com.promotion.handwriting.repository.file;

import com.promotion.handwriting.dto.file.FileToken;
import com.promotion.handwriting.util.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.createDirectories;

@Primary
@Service
@Slf4j
public class FileRepository {

    private final ResourceLoader loader;

    @Autowired
    public FileRepository(ResourceLoader loader,
                          @Value("#{systemProperties['user.dir']}") String resourcePath,
                          @Value("${directory.root}") String projectRootPath,
                          @Value("${directory.image}") String imagePath) throws IOException {
        this.loader = loader;
        this.imagePath = imagePath;
        String os = System.getProperty("os.name").toLowerCase();
        this.fileResourcePath = (os.contains("win") ? "file:///" : "file:") + resourcePath;
        if (fileResourcePath.charAt(fileResourcePath.length() - 1) == '/') {
            fileResourcePath = fileResourcePath.substring(0, fileResourcePath.length() - 1);
        }
        createDirectories(Paths.get(resourcePath + projectRootPath));
        createDirectories(Paths.get(resourcePath + imagePath));
    }

    private final String imagePath;
    private String fileResourcePath;

    public String getImageResourcePath() {
        return fileResourcePath + imagePath;
    }

    public void save(FileToken token) throws IOException {
        Resource resource = loader.getResource(getImageResourcePath() + token.getDirectory());
        Path directory = resource.getFile().toPath();
        Path location = directory.resolve(token.getFileName());
        if (!location.toFile().createNewFile())
            throw new IllegalArgumentException("토큰 식별자가 중복되었습니다.");
        FileCopyUtils.copy(token.getInputStream(), Files.newOutputStream(location));
    }

    public boolean compressAndSave(String originFilename, String targetFilename, String resourcePath) throws IOException {
        Resource resource = loader.getResource(getImageResourcePath() + resourcePath);
        String directoryPath = resource.getFile().getAbsolutePath();

        File originFile = new File(directoryPath + "/" + originFilename);
        if (!originFile.exists()) return false;
        String compressFileName = ImageUtil.compressImageName(originFilename);
        return ImageUtil.compress(originFile.getAbsolutePath(), directoryPath + "/" + compressFileName);
    }

    public boolean delete(FileToken token) {
        try {
            Resource resource = loader.getResource(getImageResourcePath() + token.getDirectory());
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

    public void deleteDirectory(String directoryPath) throws IOException {
        deleteDirectory(loader.getResource(getImageResourcePath() + directoryPath).getFile());
    }

    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            for (var file : directory.listFiles()) {
                if (file.isDirectory()) deleteDirectory(file);
                else file.delete();
            }
        }
    }
}
