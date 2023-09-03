package com.promotion.handwriting.repository.file;

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
import java.io.InputStream;
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
        String os = System.getProperty("os.name").toLowerCase();
        String fileResourcePath = (os.contains("win") ? "file:///" : "file:") + resourcePath;
        if (fileResourcePath.charAt(fileResourcePath.length() - 1) == '/') {
            fileResourcePath = fileResourcePath.substring(0, fileResourcePath.length() - 1);
        }
        imageResourcePath = fileResourcePath + imagePath;
        createDirectories(Paths.get(resourcePath + projectRootPath));
        createDirectories(Paths.get(resourcePath + imagePath));
    }

    private String imageResourcePath;

    private String getImageResourcePath() {
        return imageResourcePath;
    }

    public void save(String fileName, String directory, InputStream data) throws IOException {

        Path location = loader.getResource(getImageResourcePath() + directory)
                .getFile()
                .toPath()
                .resolve(fileName);

        if (!location.toFile().createNewFile())
            throw new IllegalArgumentException("토큰 식별자가 중복되었습니다.");

        FileCopyUtils.copy(data, Files.newOutputStream(location));
    }

    public boolean compressAndSave(String originFilename, String targetFilename, String resourcePath) throws IOException {
        Resource resource = loader.getResource(getImageResourcePath() + resourcePath);
        String directoryPath = resource.getFile().getAbsolutePath();

        File originFile = new File(directoryPath + "/" + originFilename);
        if (!originFile.exists()) return false;
        return ImageUtil.compress(originFile.getAbsolutePath(), directoryPath + "/" + targetFilename);
    }

    public void delete(String directory, String fileName) {
        try {
            String path = loader.getResource(getImageResourcePath() + directory).getFile().getAbsolutePath();
            new File(path + "/" + fileName).delete();
        } catch (IOException ex) {
            throw new IllegalArgumentException("파일 삭제 실패 : " + directory + "/" + fileName);
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
