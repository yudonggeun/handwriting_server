package com.promotion.handwriting.service;

import com.promotion.handwriting.dto.MainPromotionContentDto;
import com.promotion.handwriting.dto.MainPromotionIntroDto;

import java.io.IOException;
import java.util.List;

public interface DataService {
    List<MainPromotionContentDto> readMainPromotionContentTextFile() throws IOException;

    MainPromotionIntroDto readMainPromotionIntroTextFile();

    boolean amendContent(MainPromotionContentDto dto);

    boolean amendIntro(MainPromotionIntroDto dto);
}
