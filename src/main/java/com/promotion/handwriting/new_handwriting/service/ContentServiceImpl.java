package com.promotion.handwriting.new_handwriting.service;

import com.promotion.handwriting.dto.ContentResponse;
import com.promotion.handwriting.dto.image.ImageResponse;
import com.promotion.handwriting.new_handwriting.domain.Content;
import com.promotion.handwriting.new_handwriting.domain.Nimage;
import com.promotion.handwriting.new_handwriting.repository.ContentResponseRepository;
import com.promotion.handwriting.new_handwriting.dto.request.UpdateContentRequest;
import com.promotion.handwriting.new_handwriting.dto.CreateContentRequest;
import com.promotion.handwriting.new_handwriting.dto.SearchCondition;
import com.promotion.handwriting.new_handwriting.repository.ContentRepository;
import com.promotion.handwriting.new_handwriting.repository.ImageRepo;
import com.promotion.handwriting.new_handwriting.dto.request.CreateImageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final ContentResponseRepository contentResponseRepository;
    private final ImageRepo imageRepository;

    @Override
    public Page<ContentResponse> selectPage(Pageable pageable, SearchCondition condition) {
        return contentResponseRepository.findContent(pageable, condition);
    }

    @Override
    public Page<ImageResponse> selectImageInformation(String contentId, Pageable pageable) {
        return contentResponseRepository.findImage(contentId, pageable);
    }

    @Override
    public String createContent(CreateContentRequest request) {
        Content content = Content.builder(request.getType(), request.getTitle())
                .description(request.getDescription())
                .build();

        contentRepository.save(content);
        return content.getId();
    }

    @Override
    public void deleteContent(String contentId) {
        contentRepository.deleteById(contentId);
    }

    @Override
    public void updateContent(UpdateContentRequest request) {
        Content content = contentRepository.findById(request.getContentId()).orElseThrow(() -> new IllegalArgumentException("could you request with primary key that is in CONTENT table"));
        content.changeTitleText(request.getTitle());
        content.changeDescriptionText(request.getDescription());
    }

    @Override
    public void deleteImage(Nimage image) {
        imageRepository.delete(image);
    }

    @Override
    public Long createImage(CreateImageRequest request) {
        Nimage image = new Nimage(request.getOriginName(), request.getCompressName(), request.getPath(), request.getContentId());
        imageRepository.save(image);
        return image.getId();
    }

}
