package com.promotion.handwriting.service.business;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.MainPageDto;
import com.promotion.handwriting.dto.request.ChangeContentRequest;
import com.promotion.handwriting.dto.ImageUrlDto;
import com.promotion.handwriting.dto.request.ChangeMainPageRequest;
import com.promotion.handwriting.dto.request.CreateContentRequest;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.database.AdRepository;
import com.promotion.handwriting.repository.database.ImageRepository;
import com.promotion.handwriting.repository.file.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final AdRepository adRepository;
    private final ImageRepository imageRepository;
    private final FileRepository fileRepository;
    @Value("${spring.url.image}")
    private String imageUrl;

    @Override
    public ContentDto addContentAd(CreateContentRequest req, List<MultipartFile> images) throws IOException {
        var content = Ad.builder()
                .type(AdType.CONTENT)
                .title(req.getTitle())
                .detail(req.getDescription())
                .resourcePath("/content/" + UUID.randomUUID())
                .build();

        for (MultipartFile file : images) {
            content.addImage(file, fileRepository);
        }
        return content.contentDto(imageUrl);
    }

    @Override
    public String addImage(MultipartFile imageFile, long adId) throws IOException {
        var ad = adRepository.getReferenceById(adId);
        return ad.addImage(imageFile, fileRepository).getImageName();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ContentDto> getContentDtos(AdType type, Pageable pageable) {
        Page<Ad> pages = adRepository.findByType(type, pageable);
        List<ContentDto> list = pages.getContent().stream().map(content -> content.contentDto(imageUrl)).collect(Collectors.toList());
        return new PageImpl<>(list, pageable, pages.getTotalElements());
    }

    @Override
    public Page<ImageUrlDto> getImageUrls(String contentId, Pageable pageable) {
        return imageRepository.findByAdId(Long.parseLong(contentId), pageable)
                .map(image -> image.urlDto(imageUrl));
    }

    @Override
    public void updateContent(ChangeContentRequest request) {
        Ad ad = adRepository.getReferenceById(request.getId());
        ad.setDetail(request.getDescription());
        ad.setTitle(request.getTitle());
    }

    @Override
    public void updateIntro(ChangeMainPageRequest req, MultipartFile file) throws IOException {
        var content = adRepository.findByType(AdType.INTRO, PageRequest.of(0, 1)).getContent().get(0);

        content.setDetail(req.getDescription());

        var resourcePath = content.getResourcePath();

        if (file != null) {
            content.addImage(file, fileRepository);
            content.getImages().forEach(image -> {
                fileRepository.delete(resourcePath, image.getImageName());
                fileRepository.delete(resourcePath, image.getZipImageName());
            });
        }
    }

    @Override
    public void deleteAd(long id) throws IOException {
        var content = adRepository.findWithImageById(id);
        fileRepository.deleteDirectory(content.getResourcePath());
        imageRepository.deleteAllByAdId(id);
        adRepository.deleteById(id);
    }

    @Override
    public void deleteImages(List<String> fileNames, long adId) {
        var content = adRepository.findWithImageById(adId);
        List<Image> deleteImage = content.getImages().stream()
                .filter(image -> fileNames.contains(image.getImageName()))
                .collect(Collectors.toList());

        for (var image : deleteImage) {
            fileRepository.delete(content.getResourcePath(), image.getImageName());
            fileRepository.delete(content.getResourcePath(), image.getZipImageName());
        }
        imageRepository.deleteAllInBatch(deleteImage);
    }

    @Override
    public MainPageDto mainPageData() {
        try {
            Ad mainPage = adRepository.findByType(AdType.INTRO);
            Image image = mainPage.getImages().get(0);
            String mainPageImageUrl = image.urlDto(imageUrl).getOriginal();
            return new MainPageDto(mainPage.getTitle(), mainPageImageUrl, mainPage.getDetail());
        } catch (NoResultException | NonUniqueResultException e) {
            throw new IllegalStateException("메인 페이지 정보 칼럼이 1개가 아닙니다. 데이터를 확인해주세요");
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalStateException("메인 페이지 이미지 정보가 존재하지 않습니다.");
        }
    }
}