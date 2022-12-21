package com.promotion.handwriting.service;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.IntroDto;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.AdRepository;
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
                .map(image -> "/api" + ad.getResourcePath() + image.getImageName())
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
        List<Ad> intros = adRepository.findALLByType(AdType.INTRO);
        if (intros.size() != 1) {
            throw new RuntimeException("intro 데이터가 " + intros.size() + " 만큼 존재합니다. intro 데이터는 하나만 있어야 합니다. 데어터를 수정해주세요.");
        }
        return new IntroDto(intros.get(0));
    }

    @Override
    public boolean amendContent(ContentDto dto) {
        try {
            Ad ad = adRepository.getReferenceById(Long.parseLong(dto.getId()));
            ad.setDetail(dto.getDescription());
            ad.setTitle(dto.getTitle());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean amendIntro(IntroDto dto) {
        List<Ad> intros = adRepository.findALLByType(AdType.INTRO);
        if (intros.size() != 1) {
            throw new RuntimeException("intro 데이터가 " + intros.size() + " 만큼 존재합니다. intro 데이터는 하나만 있어야 합니다. 데어터를 수정해주세요.");
        }
        Ad intro = intros.get(0);
        String detail = "";
        for (String comment : dto.getComments())
            detail += comment + "\n\r";

        intro.setDetail(detail);
        return true;
    }
}
