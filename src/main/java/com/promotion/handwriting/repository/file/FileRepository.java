package com.promotion.handwriting.repository.file;

import com.promotion.handwriting.dto.file.FileToken;

import java.io.IOException;

public interface FileRepository {
    void save(FileToken file) throws IOException;

    boolean compressAndSave(String originFilename, String targetFilename, String resourcePath) throws IOException;

    boolean delete(FileToken token);

    void deleteDirectory(String directoryPath) throws IOException;

}
