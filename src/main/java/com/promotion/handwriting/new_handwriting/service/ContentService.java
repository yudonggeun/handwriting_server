package com.promotion.handwriting.new_handwriting.service;

import com.promotion.handwriting.new_handwriting.domain.Nimage;
import com.promotion.handwriting.new_handwriting.request.UpdateContentRequest;
import com.promotion.handwriting.new_handwriting.dto.ContentCreateDto;
import com.promotion.handwriting.new_handwriting.dto.SearchCondition;
import com.promotion.handwriting.new_handwriting.request.CreateImageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface ContentService {

    /**
     * 기본 페이지 조회
     */
    void selectPage(Pageable pageable, Sort sort, SearchCondition dto);

    /**
     * 이미지 리스트 조회
     */
    void selectImageInformation(String contentId, Pageable pageable);

    /**
     * 홍보 데이터 추가
     */
    String createContent(ContentCreateDto dto);

    /**
     * 홍보 데이터 삭제
     */
    void deleteContent(String contentId);

    /**
     * 홍보 데이터 수정
     */
    void updateContent(UpdateContentRequest dto);

    /**
     * 이미지 삭제
     */
    void deleteImage(Nimage image);

    /**
     * 이미지 추가
     */
    Long createImage(CreateImageRequest request);
}
