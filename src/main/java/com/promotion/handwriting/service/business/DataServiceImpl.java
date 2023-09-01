package com.promotion.handwriting.service.business;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.IntroDto;
import com.promotion.handwriting.dto.SimpleContentDto;
import com.promotion.handwriting.dto.image.UrlImageDto;
import com.promotion.handwriting.dto.request.CreateContentRequest;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.database.AdRepository;
import com.promotion.handwriting.repository.database.ImageRepository;
import com.promotion.handwriting.repository.file.FileRepository;
import com.promotion.handwriting.util.UrlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Override
    public ContentDto addContentAd(CreateContentRequest req, List<MultipartFile> images) throws IOException {
        var content = Ad.builder()
                .type(AdType.CONTENT)
                .title(req.getTitle())
                .detail(req.getDescription())
                .resourcePath("/content/" + UUID.randomUUID())
                .build();

        for (MultipartFile file : images) {
            content.createImage(file, fileRepository);
        }
        return content.contentDto();
    }

    @Override
    public String addImage(MultipartFile imageFile, long adId) throws IOException {
        var ad = adRepository.getReferenceById(adId);
        return ad.createImage(imageFile, fileRepository);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ContentDto> getContentDtos(AdType type) {
        return adRepository.findByType(type).stream()
                .map(Ad::contentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UrlImageDto> getImageUrlAtContent(String contentId, int start, int end) {
        return adRepository.findWithImageById(Long.parseLong(contentId))
                .getImages().stream()
                .map(image -> UrlImageDto.make(
                        UrlUtil.getImageUrl(adRepository.findWithImageById(Long.parseLong(contentId)).getResourcePath(), image.getImageName()),
                        UrlUtil.getImageUrl(adRepository.findWithImageById(Long.parseLong(contentId)).getResourcePath(), image.getCompressImageName())))
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateContent(SimpleContentDto dto) {
        Ad ad = adRepository.getReferenceById(dto.getId());
        ad.setDetail(dto.getDescription());
        ad.setTitle(dto.getTitle());
        return true;
    }

    @Override
    public boolean updateIntro(IntroDto dto, MultipartFile file) throws IOException {
        var content = adRepository.findByType(AdType.INTRO).get(0);

        StringBuilder sb = new StringBuilder();
        //todo 단락을 나누는 기준은 sperate로 하면 이상하다.
        dto.getComments().forEach(comment -> sb.append(comment).append(IntroDto.separate));
        sb.deleteCharAt(sb.length() - 1);

        content.setDetail(sb.toString());

        if (file != null) {
            content.createImage(file, fileRepository);
            content.getImages()
                    .forEach(image -> image.delete(fileRepository, content.getResourcePath()));
        }
        return true;
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
            image.delete(fileRepository, content.getResourcePath());
        }
        imageRepository.deleteAllInBatch(deleteImage);
    }
}