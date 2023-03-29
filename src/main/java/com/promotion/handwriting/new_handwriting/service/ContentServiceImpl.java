package com.promotion.handwriting.new_handwriting.service;

import com.promotion.handwriting.new_handwriting.domain.Content;
import com.promotion.handwriting.new_handwriting.domain.Nimage;
import com.promotion.handwriting.new_handwriting.request.UpdateContentRequest;
import com.promotion.handwriting.new_handwriting.dto.ContentCreateDto;
import com.promotion.handwriting.new_handwriting.dto.SearchCondition;
import com.promotion.handwriting.new_handwriting.repository.ContentRepository;
import com.promotion.handwriting.new_handwriting.repository.ImageRepo;
import com.promotion.handwriting.new_handwriting.request.CreateImageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final ImageRepo imageRepository;

    @Override
    public void selectPage(Pageable pageable, Sort sort, SearchCondition dto) {

    }

    @Override
    public void selectImageInformation(String contentId, Pageable pageable) {

    }

    @Override
    public String createContent(ContentCreateDto dto) {
        Content content = Content.builder("CONTENT_" + dto.getType() + "_" + UUID.randomUUID(), dto.getTitle())
                .description(dto.getDescription())
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
