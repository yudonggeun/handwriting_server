package com.promotion.handwriting.service.business;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.image.UrlImageDto;
import com.promotion.handwriting.dto.IntroDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DataService {

    //인트로 생성
    IntroDto createIntroAd() throws IOException;
    //컨텐츠 추가
    ContentDto addContentAd(ContentDto dto) throws IOException;
    //컨텐츠 이미지 추가
    String addImageAtAd(MultipartFile imageFile, long adId);

    //인트로 조회
    IntroDto getIntroDto();
    //컨텐츠 조회
    Optional<ContentDto> getContentDtoById(long id);
    //컨턴츠 조회
    List<ContentDto> getContentDtos() throws IOException;

    //컨텐트 이미지 조회
    List<UrlImageDto> getImageUrlAtContent(String contentId, int start, int end);

    //인트로 수정
    boolean updateIntro(IntroDto dto, MultipartFile file) throws IOException;
    //컨텐츠 수정
    boolean updateContent(ContentDto dto);

    //컨텐츠 삭제
    void deleteAd(long id);
    //이미지 삭제
    void deleteImages(List<String> fileNames, long adId);
}
