package com.promotion.handwriting.service.business;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.IntroDto;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.AdRepository;
import com.promotion.handwriting.repository.ImageRepository;
import com.promotion.handwriting.util.UrlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Primary
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final AdRepository adRepository;
    private final ImageRepository imageRepository;

    @Override
    public IntroDto createIntroAd() throws IOException {
        Ad intro = adRepository.findIntroAd();
        if (intro == null) {
            intro = Ad.createAd(AdType.INTRO, "", "소개글을 작성해주세요", "/intro");
            Image image = Image.builder()
                    .priority(0)
                    .imageName("no_image_jpg")
                    .compressImageName("no_image_jpg")
                    .build();
            intro.addImage(image);
            intro = adRepository.save(intro);
        }
        return IntroDto.convert(intro);
    }

    @Override
    public ContentDto createContentAd(ContentDto dto) throws IOException {
        Ad ad = Ad.createAd(AdType.CONTENT, dto.getTitle(), dto.getDescription(), "/content/" + UUID.randomUUID());

        int count = dto.getImages().size();
        if (count != dto.getOriginImages().size()) log.warn("원본 파일과 압축파일의 개수가 같지 않습니다.");

        for (int i = 0; i < count; i++) {
            String originImage = dto.getOriginImages().get(i);
            String compressImage = dto.getImages().get(i);
            Image image = Image.builder()
                    .priority(Integer.MAX_VALUE)
                    .imageName(originImage)
                    .compressImageName(compressImage)
                    .build();
            ad.addImage(image);
        }
        adRepository.save(ad);
        return ContentDto.convert(ad);
    }

    @Override
    public String createImageAtAd(String imageFile, String compressImageFile, long adId) {
        Ad ad = adRepository.findById(adId).orElseThrow();
        Image image = Image.builder()
                .priority(Integer.MAX_VALUE)
                .imageName(imageFile)
                .compressImageName(compressImageFile)
                .build();
        log.info("create image : " + image.toString());
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
    public List<String> getCompressImageSrcByContentId(String id, int start, int count) {
        Ad ad = adRepository.findAdWithImagesById(Long.parseLong(id));
        return ad.getImages()
                .stream()
                .map(image -> {
                    //압축 이미지가 없으면 원본 반환
                    String imageName = image.getCompressImageName() == null ? image.getImageName() : image.getCompressImageName();
                    return UrlUtil.getImageUrl(ad.getResourcePath(), imageName);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getOriginImageSrcByContentId(String id, int start, int count) {
        Ad ad = adRepository.findAdWithImagesById(Long.parseLong(id));
        return ad.getImages()
                .stream()
                .map(image -> UrlUtil.getImageUrl(ad.getResourcePath(), image.getImageName()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ContentDto> getContentDtoById(long id) {
        Optional<Ad> findAd = adRepository.findById(id);
        return Optional.ofNullable(ContentDto.convert(findAd.orElse(null)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ContentDto> getContentDtos() {
        return adRepository.findContents().stream()
                .map(ContentDto::convert)
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
    public List<String> deleteImages(List<String> fileNames, long adId) {
        Ad ad = adRepository.findAdWithImagesById(adId);
        List<Image> deleteImage = ad.getImages().stream()
                .filter(image -> fileNames.contains(image.getImageName()) || fileNames.contains(image.getCompressImageName()))
                .collect(Collectors.toList());
        imageRepository.deleteAllInBatch(deleteImage);

        List<String> result = new ArrayList<>();
        result.addAll(deleteImage.stream().map(Image::getCompressImageName).collect(Collectors.toList()));
        result.addAll(deleteImage.stream().map(Image::getImageName).collect(Collectors.toList()));
        return result;
    }
}
