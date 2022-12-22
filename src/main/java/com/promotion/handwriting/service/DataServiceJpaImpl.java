package com.promotion.handwriting.service;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.IntroDto;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.AdRepository;
import com.promotion.handwriting.util.UrlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class DataServiceJpaImpl implements DataService {

    private final AdRepository adRepository;

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
        return new IntroDto(adRepository.findByType(AdType.INTRO));
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
        String detail = "";
        for (String comment : dto.getComments())
            detail += comment + "/#";

        intro.setDetail(detail);
        return true;
    }
}
