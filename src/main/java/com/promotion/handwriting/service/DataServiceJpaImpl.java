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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class DataServiceJpaImpl implements DataService {

    private final AdRepository adRepository;
    private final ImageRepository imageRepository;

    @Override
    public List<String> getImageSrcByContentId(String id, int start, int count) {
        Ad ad = adRepository.findAdWithImagesById(Long.parseLong(id));
        return ad.getImages()
                .stream()
                .map(image -> UrlUtil.getImageUrl(ad.getResourcePath(), image))
                .collect(Collectors.toList());
    }

    @Override
    public List<ContentDto> getContentDtos() {
        return adRepository.findALLByType(AdType.CONTENT).stream()
                .map(ContentDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public IntroDto getIntroDto() {
        Ad intro = adRepository.findByType(AdType.INTRO);
        IntroDto dto = new IntroDto();
        dto.setComments(Arrays.stream(intro.getDetail().split(IntroDto.separate)).collect(Collectors.toList()));
        dto.setImage(UrlUtil.getImageUrl(intro.getResourcePath(), intro.getImages().get(0)));
        return dto;
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
        Ad intro = adRepository.findByType(AdType.INTRO);

        StringBuilder sb = new StringBuilder();
        dto.getComments().forEach(comment -> sb.append(comment).append(IntroDto.separate));
        sb.deleteCharAt(sb.length()-1);

        intro.setDetail(sb.toString());
        if(dto.getImage() != null) {
            List<Image> images = intro.getImages();
            imageRepository.deleteAllById(images.stream().mapToLong(Image::getId).boxed().collect(Collectors.toList()));
            imageRepository.save(new Image(intro.getId(), 0, dto.getImage()));
        }
        return true;
    }
}
