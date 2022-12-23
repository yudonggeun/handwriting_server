package com.promotion.handwriting.service;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.IntroDto;

import java.io.IOException;
import java.util.List;

public interface DataService {

    /**
     * create intro Ad
     */
    void createIntroAd() throws IOException;

    /**
     * create content Ad
     */
    void createContentAd(ContentDto dto) throws IOException;

    List<ContentDto> getContentDtos() throws IOException;

    IntroDto getIntroDto();

    List<String> getImageSrcByContentId(String id, int start, int count) throws IOException;

    boolean amendContent(ContentDto dto);

    boolean amendIntro(IntroDto dto);

    /**
     * delete Ad
     */
    void deleteAd(long id);
}
