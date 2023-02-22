package com.promotion.handwriting.dto.file;

import com.promotion.handwriting.enums.FileTokenType;

import java.io.InputStream;

public interface FileToken {
    /**
     * @return 토큰 타입
     */
    FileTokenType getTokenType();

    /**
     * @return 파일 디렉토리 경로
     */
    String getDirectory();

    /**
     * @return 파일이름
     */
    String getFileName();

    /**
     * @return 절대 경로
     */
    String getAbsolutePath();

    /**
     * @return 식별자
     */
    String getIdentifier();

    /**
     * @return input stream
     */
    InputStream getInputStream();
}
