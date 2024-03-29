package com.promotion.handwriting.service.business;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.ImageUrlDto;
import com.promotion.handwriting.dto.MainPageDto;
import com.promotion.handwriting.dto.request.ChangeContentRequest;
import com.promotion.handwriting.dto.request.ChangeMainPageRequest;
import com.promotion.handwriting.dto.request.CreateContentRequest;
import com.promotion.handwriting.dto.request.SearchContentsRequest;
import com.promotion.handwriting.enums.AdType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DataService {

    //컨텐츠 추가
    ContentDto newContent(CreateContentRequest request, List<MultipartFile> images);

    //컨텐츠 이미지 추가
    String newImage(MultipartFile imageFile, long adId);

    //컨텐츠 조회
    Page<ContentDto> getContentDtos(SearchContentsRequest request, Pageable pageable);

    //컨텐트 이미지 조회
    Page<ImageUrlDto> getImageUrls(String contentId, Pageable pageable);

    //인트로 수정
    void updateMainPage(ChangeMainPageRequest request, MultipartFile file);

    //컨텐츠 수정
    void updateContent(ChangeContentRequest request);

    //컨텐츠 삭제
    void deleteContent(long id);

    //이미지 삭제
    void deleteImages(List<Long> images, long adId);

    MainPageDto mainPageData();
}
