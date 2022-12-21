package com.promotion.handwriting.service;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.IntroDto;

import java.io.IOException;
import java.util.List;

public interface DataService {
    List<String> getImageSrcByContentId(String id, int start, int count) throws IOException;

    List<ContentDto> getContentDtos() throws IOException;

    IntroDto getIntroDto();

    boolean amendContent(ContentDto dto);

    boolean amendIntro(IntroDto dto);
}
