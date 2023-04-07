package com.promotion.handwriting.new_handwriting.service;

import com.promotion.handwriting.dto.ContentResponse;
import com.promotion.handwriting.dto.image.ImageResponse;
import com.promotion.handwriting.new_handwriting.domain.Nimage;
import com.promotion.handwriting.new_handwriting.dto.request.UpdateContentRequest;
import com.promotion.handwriting.new_handwriting.dto.CreateContentRequest;
import com.promotion.handwriting.new_handwriting.dto.SearchCondition;
import com.promotion.handwriting.new_handwriting.dto.request.CreateImageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContentService {

    /**
     * 기본 페이지 조회
     */
    Page<ContentResponse> selectPage(Pageable pageable, SearchCondition condition);

    /**
     * 이미지 리스트 조회
     */
    Page<ImageResponse> selectImageInformation(String contentId, Pageable pageable);

    /**
     * 홍보 데이터 추가
     */
    String createContent(CreateContentRequest request);

    /**
     * 홍보 데이터 삭제
     */
    void deleteContent(String contentId);

    /**
     * 홍보 데이터 수정
     */
    void updateContent(UpdateContentRequest request);

    /**
     * 이미지 삭제
     */
    void deleteImage(Nimage image);

    /**
     * 이미지 추가
     */
    Long createImage(CreateImageRequest request);
}
