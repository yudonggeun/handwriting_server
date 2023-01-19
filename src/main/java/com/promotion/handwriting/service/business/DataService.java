package com.promotion.handwriting.service.business;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.IntroDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DataService {

    IntroDto createIntroAd() throws IOException;

    ContentDto createContentAd(ContentDto dto) throws IOException;

    String createImageAtAd(String imageFile, long adId);

    Optional<ContentDto> getContentDtoById(long id);

    List<ContentDto> getContentDtos() throws IOException;

    IntroDto getIntroDto();

    List<String> getImageSrcByContentId(String id) throws IOException;

    boolean amendContent(ContentDto dto);

    boolean amendIntro(IntroDto dto);

    void deleteAd(long id);

    void deleteImages(List<String> fileNames, long adId);
}
