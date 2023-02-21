package com.promotion.handwriting.dto.child;

import com.promotion.handwriting.dto.Abstract.AbstractFileToken;
import com.promotion.handwriting.dto.parent.FileToken;
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


    public static FileToken deleteDirectory(String resourcePath) {
        return new LocalFileToken(null, resourcePath, "");
    }
    @Override
    public FileTokenType getTokenType() {
        return this.type;
    }
}
