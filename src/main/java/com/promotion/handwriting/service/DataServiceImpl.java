package com.promotion.handwriting.service;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.IntroDto;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.AdRepository;
import com.promotion.handwriting.repository.ImageRepository;
import com.promotion.handwriting.util.UrlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final AdRepository adRepository;
    private final ImageRepository imageRepository;

    @Override
    public IntroDto createIntroAd() throws IOException {
        Ad intro = adRepository.findIntroAd();
        if(intro == null){
            intro = new Ad(AdType.INTRO, "", "소개글을 작성해주세요", "/intro");
            Image image = Image.builder().priority(0).imageName("no_image_jpg").build();
            intro.addImage(image);
            intro = adRepository.save(intro);
        }
        return IntroDto.convert(intro);
    }

    @Override
    public ContentDto createContentAd(ContentDto dto) throws IOException {
        Ad ad = new Ad(AdType.CONTENT, dto.getTitle(), dto.getDescription(), "/content/" + UUID.randomUUID());
        dto.getImages().forEach(imageName -> {
            Image image = Image.builder().priority(Integer.MAX_VALUE).imageName(imageName).build();
            ad.addImage(image);
        });
        adRepository.save(ad);
        return ContentDto.convert(ad);
    }

    @Override
    public String createImageAtAd(String imageFile, long adId) {
        Ad ad = adRepository.findById(adId).orElseThrow();
        Image image = Image.builder().priority(Integer.MAX_VALUE).imageName(imageFile).build();
        ad.addImage(image);
        return imageFile;
    }

    @Transactional(readOnly = true)
    @Override
    public IntroDto getIntroDto() {
        Ad intro = adRepository.findIntroAd();
        return IntroDto.convert(intro);
    }

    @Transactional(readOnly = true)
    @Override
    public ContentDto getContentDtoById(long id) {
        Ad findAd = adRepository.getReferenceById(id);
        return ContentDto.convert(findAd);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ContentDto> getContentDtos() {
        return adRepository.findContents().stream()
                .map(ContentDto::convert)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getImageSrcByContentId(String id) {
        Ad ad = adRepository.findAdWithImagesById(Long.parseLong(id));
        return ad.getImages()
                .stream()
                .map(image -> UrlUtil.getImageUrl(ad.getResourcePath(), image))
                .collect(Collectors.toList());
    }

    @Override
    public boolean amendContent(ContentDto dto) {
        Ad ad = adRepository.getReferenceById(Long.parseLong(dto.getId()));
        ad.setDetail(dto.getDescription());
        ad.setTitle(dto.getTitle());
        return true;
    }

    @Override
    public boolean amendIntro(IntroDto dto) {
        Ad intro = adRepository.findIntroAd();

        StringBuilder sb = new StringBuilder();
        dto.getComments().forEach(comment -> sb.append(comment).append(IntroDto.separate));
        sb.deleteCharAt(sb.length() - 1);

        intro.setDetail(sb.toString());
        if (dto.getImage() != null) {
            List<Image> images = intro.getImages();
            Image image = images.get(0);
            image.setImageName(dto.getImage());
        }
        return true;
    }

    @Override
    public void deleteAd(long id) {
        imageRepository.deleteAllByAd(id);
        adRepository.deleteById(id);
    }

    @Override
    public void deleteImages(List<String> fileNames, long adId) {
        Ad ad = adRepository.findAdWithImagesById(adId);
        List<Image> deleteImage = ad.getImages().stream()
                .filter(image -> fileNames.contains(image.getImageName()))
                .collect(Collectors.toList());
        imageRepository.deleteAllInBatch(deleteImage);
    }
}
