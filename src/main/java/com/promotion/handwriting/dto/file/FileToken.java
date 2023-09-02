package com.promotion.handwriting.dto.file;

import com.promotion.handwriting.enums.FileTokenType;

import java.io.InputStream;

public interface FileToken {

    /**
     * @return 파일 디렉토리 경로
     */
    String getDirectory();

    /**
     * @return 파일이름
     */
    String getFileName();
    /**
     * @return input stream
     */
    InputStream getInputStream();
}
