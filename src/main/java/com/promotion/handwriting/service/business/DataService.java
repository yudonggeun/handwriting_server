package com.promotion.handwriting.service.business;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.request.ContentChangeRequest;
import com.promotion.handwriting.dto.image.UrlImageDto;
import com.promotion.handwriting.dto.IntroDto;
import com.promotion.handwriting.dto.request.CreateContentRequest;
import com.promotion.handwriting.enums.AdType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DataService {

    //컨텐츠 추가
    ContentDto addContentAd(CreateContentRequest request, List<MultipartFile> images) throws IOException;
    //컨텐츠 이미지 추가
    String addImage(MultipartFile imageFile, long adId) throws IOException;

    //컨텐츠 조회
    Page<ContentDto> getContentDtos(AdType type, Pageable pageable) throws IOException;

    //컨텐트 이미지 조회
    Page<UrlImageDto> getImageUrls(String contentId, Pageable pageable);

    //인트로 수정
    void updateIntro(IntroDto dto, MultipartFile file) throws IOException;
    //컨텐츠 수정
    void updateContent(ContentChangeRequest dto);

    //컨텐츠 삭제
    void deleteAd(long id) throws IOException;
    //이미지 삭제
    void deleteImages(List<String> fileNames, long adId);

    IntroDto mainPageData();
}
