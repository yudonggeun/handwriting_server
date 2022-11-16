package com.promotion.handwriting.service;

import com.promotion.handwriting.dto.MainPromotionContentDto;
import com.promotion.handwriting.dto.MainPromotionIntroDto;

public interface DataService {
    MainPromotionContentDto readMainPromotionContentTextFile();

    MainPromotionIntroDto readMainPromotionIntroTextFile();

    boolean amendContent(MainPromotionContentDto dto);

    boolean amendIntro(MainPromotionIntroDto dto);
}
