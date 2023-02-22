package com.promotion.handwriting.repository.file;

import com.promotion.handwriting.dto.file.FileToken;

import java.io.IOException;

public interface FileRepository {
    boolean save(FileToken file);

    boolean compressAndSave(String originFilename, String targetFilename, String resourcePath) throws IOException;

    boolean delete(FileToken token);

    boolean deleteDirectory(FileToken token);

}
