package com.promotion.handwriting.service.business;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.ImageUrlDto;
import com.promotion.handwriting.dto.MainPageDto;
import com.promotion.handwriting.dto.request.ChangeContentRequest;
import com.promotion.handwriting.dto.request.ChangeMainPageRequest;
import com.promotion.handwriting.dto.request.CreateContentRequest;
import com.promotion.handwriting.dto.request.SearchContentsRequest;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.database.AdRepository;
import com.promotion.handwriting.repository.database.JpaImageRepository;
import com.promotion.handwriting.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NonUniqueResultException;
import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final AdRepository adRepository;
    private final JpaImageRepository imageRepository;
    private final FileRepository fileRepository;
    @Value("${spring.url.image}")
    private String imageUrl;

    @Override
    public ContentDto newContent(CreateContentRequest req, List<MultipartFile> images) {
        var content = adRepository.save(Ad.builder()
                .type(AdType.CONTENT)
                .title(req.getTitle())
                .detail(req.getDescription())
                .build());

        if (images != null) {
            images.forEach(file -> content.addImage(file, fileRepository));
        }
        return content.contentDto();
    }

    @Override
    public String newImage(MultipartFile imageFile, long contentId) {
        return adRepository.findById(contentId)
                .addImage(imageFile, fileRepository)
                .getImageUrl();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ContentDto> getContentDtos(SearchContentsRequest request, Pageable pageable) {
        Page<Ad> pages = adRepository.findByType(request.getType(), pageable);
        List<ContentDto> list = pages.getContent().stream().map(content -> content.contentDto()).collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pages.getTotalElements());
    }

    @Override
    public Page<ImageUrlDto> getImageUrls(String contentId, Pageable pageable) {
        return imageRepository.findByAdId(Long.parseLong(contentId), pageable)
                .map(image -> image.urlDto());
    }

    @Override
    public void updateContent(ChangeContentRequest request) {
        Ad ad = adRepository.getReferenceById(request.getId());
        ad.setDetail(request.getDescription());
        ad.setTitle(request.getTitle());
    }

    @Override
    public void updateMainPage(ChangeMainPageRequest req, MultipartFile file) {
        var content = adRepository.findByType(AdType.INTRO);
        content.setTitle(req.getTitle());
        content.setDetail(req.getDescription());

        if (file != null) {
            content.getImages().forEach(image -> fileRepository.delete(image));
            content.addImage(file, fileRepository);
        }
    }

    @Override
    public void deleteContent(long id) {
        imageRepository.findByAdId(id).forEach(image -> fileRepository.delete(image));
        imageRepository.deleteAllByAdId(id);
        adRepository.deleteById(id);
    }

    @Override
    public void deleteImages(List<Long> images, long contentId) {
        imageRepository.findByIdIn(images).forEach(image -> fileRepository.delete(image));
        imageRepository.deleteAllByIdIn(images);
    }

    @Override
    public MainPageDto mainPageData() {
        try {
            Ad mainPage = adRepository.findByType(AdType.INTRO);
            String originalImageUrl = null;
            if(mainPage.getImages().size() > 0)
                originalImageUrl = mainPage.getImages().get(0).getImageUrl();
            return new MainPageDto(mainPage.getTitle(), originalImageUrl, mainPage.getDetail());
        } catch (NonUniqueResultException | IncorrectResultSizeDataAccessException e) {
            throw new IllegalStateException("메인 페이지 정보 칼럼이 1개가 아닙니다. 데이터를 확인해주세요");
        }
    }
}