package com.promotion.handwriting.service;

import com.promotion.handwriting.dto.MainPromotionContentDto;
import com.promotion.handwriting.dto.MainPromotionIntroDto;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.AdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
        Ad ad = adRepository.getReferenceById(Long.parseLong(id));
        return ad.getImages()
                .stream()
                .map(image -> "/api" + ad.getResourcePath() + image.getImageName())
                .collect(Collectors.toList());
    }

    @Override
    public List<MainPromotionContentDto> readMainPromotionContentTextFile() {
        return adRepository.selectType(AdType.CONTENT).stream()
                .map(MainPromotionContentDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public MainPromotionIntroDto readMainPromotionIntroTextFile() {
        List<Ad> intros = adRepository.selectType(AdType.INTRO);
        if (intros.size() != 1) {
            throw new RuntimeException("intro 데이터가 " + intros.size() + " 만큼 존재합니다. intro 데이터는 하나만 있어야 합니다. 데어터를 수정해주세요.");
        }
        return new MainPromotionIntroDto(intros.get(0));
    }

    @Override
    public boolean amendContent(MainPromotionContentDto dto) {
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
    public boolean amendIntro(MainPromotionIntroDto dto) {
        List<Ad> intros = adRepository.selectType(AdType.INTRO);
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
