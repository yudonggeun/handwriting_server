package com.promotion.handwriting.dto.file;

import com.promotion.handwriting.enums.FileTokenType;

import java.io.InputStream;

public class LocalFileToken extends AbstractFileToken {

    public LocalFileToken(InputStream inputStream, String resourcePath, String filename) {
        super(inputStream, resourcePath, filename);
        super.type = FileTokenType.LOCAL;
    }

    public static FileToken save(InputStream inputStream, String resourcePath, String filename) {
        return new LocalFileToken(inputStream, resourcePath, filename);
    }

    public static FileToken delete( String resourcePath, String filename) {
        return new LocalFileToken(null, resourcePath, filename);
    }
}
