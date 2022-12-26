package com.promotion.handwriting.service;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.IntroDto;

import java.io.IOException;
import java.util.List;

public interface DataService {

    /**
     * create intro Ad
     */
    IntroDto createIntroAd() throws IOException;

    /**
     * create content Ad
     */
    ContentDto createContentAd(ContentDto dto) throws IOException;

    String createImageAtAd(String imageFile, long adId);

    ContentDto getContentDtoById(long id);

    List<ContentDto> getContentDtos() throws IOException;

    IntroDto getIntroDto();

    List<String> getImageSrcByContentId(String id) throws IOException;

    boolean amendContent(ContentDto dto);

    boolean amendIntro(IntroDto dto);

    /**
     * delete Ad
     */
    void deleteAd(long id);

    void deleteImages(List<String> fileNames, long adId);
}
