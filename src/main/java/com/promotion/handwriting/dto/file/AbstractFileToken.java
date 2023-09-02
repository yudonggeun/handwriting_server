package com.promotion.handwriting.dto.file;

import com.promotion.handwriting.enums.FileTokenType;

import java.io.InputStream;

/**
 * The class for the FileService input variable.
 * <p>
 * inputStream is the information about the file that the file service will cover.
 * resourcePath is the directory path information that the file service stores.
 * filename is the name of the file that contains the extension and is used as the identifier. Pay attention to duplication.
 */

/**
 * FileService 입력 변수에 대한 클래스입니다.
 *
 * inputStream은 파일 서비스에서 다룰 파일의 정보입니다.
 * resourcePath는 파일 서비스에서 저장할 디렉토리 경로 정보입니다. 경로 마지막에 '/'를 제거하세요.
 * filename은 확장자를 포함한 파일이름으로써 식별자로서 사용합니다. 중복에 유의하세요.
 */
public abstract class AbstractFileToken implements FileToken {
    private final InputStream inputStream;
    private final String resourcePath;
    private final String filename;
    protected FileTokenType type;

    public AbstractFileToken(InputStream inputStream, String resourcePath, String filename) {
        this.inputStream = inputStream;
        this.resourcePath = resourcePath;
        this.filename = filename;
    }

    @Override
    public String getDirectory() {
        return resourcePath;
    }

    @Override
    public String getFileName() {
        return filename;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }
}
