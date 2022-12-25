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
import java.util.Arrays;
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
    public void createIntroAd() throws IOException {
        Ad intro = adRepository.findIntroAd();
        if(intro == null){
            Ad ad = new Ad(AdType.INTRO, "", "소개글을 작성해주세요", "/intro", null);
            Image image = new Image(ad, 0, "no_image_jpg");
            adRepository.save(ad);
            imageRepository.save(image);
        }
    }

    @Override
    public void createContentAd(ContentDto dto) throws IOException {
        Ad ad = new Ad(AdType.CONTENT, dto.getTitle(), dto.getDescription(), "/content/" + UUID.randomUUID(), null);
        List<Image> newImages = dto.getImages().stream().map(imageName -> new Image(ad, Integer.MAX_VALUE, imageName)).collect(Collectors.toList());
        adRepository.save(ad);
        imageRepository.saveAll(newImages);
    }

    @Transactional(readOnly = true)
    @Override
    public IntroDto getIntroDto() {
        Ad intro = adRepository.findIntroAd();
        IntroDto dto = new IntroDto();
        dto.setComments(Arrays.stream(intro.getDetail().split(IntroDto.separate)).collect(Collectors.toList()));
        dto.setImage(UrlUtil.getImageUrl(intro.getResourcePath(), intro.getImages().get(0)));
        return dto;
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
            imageRepository.deleteAllByAd(intro);
            imageRepository.save(new Image(intro, 0, dto.getImage()));
        }
        return true;
    }

    @Override
    public void deleteAd(long id) {
        imageRepository.deleteAllByAd(Ad.getProxyAd(id));
        adRepository.deleteById(id);
    }
}
