package com.promotion.handwriting.service.business;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.IntroDto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DataService {

    IntroDto createIntroAd() throws IOException;

    ContentDto createContentAd(ContentDto dto) throws IOException;

    String createImageAtAd(String imageFile, String compressImageFile, long adId);

    Optional<ContentDto> getContentDtoById(long id);

    List<ContentDto> getContentDtos() throws IOException;

    IntroDto getIntroDto();

    List<String> getCompressImageSrcByContentId(String id, int start, int count) throws IOException;

    List<String> getOriginImageSrcByContentId(String id, int start, int count) throws IOException;

    boolean amendContent(ContentDto dto);

    boolean amendIntro(IntroDto dto);

    void deleteAd(long id);

    List<String> deleteImages(List<String> fileNames, long adId);
}
