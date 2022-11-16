package com.promotion.handwriting.service;

import com.promotion.handwriting.dto.MainPromotionContentDto;
import com.promotion.handwriting.dto.MainPromotionIntroDto;

public class DataServiceImpl implements DataService{
    @Override
    public MainPromotionContentDto readMainPromotionContentTextFile() {
        return null;
    }

    @Override
    public MainPromotionIntroDto readMainPromotionIntroTextFile() {
        return null;
    }

    @Override
    public boolean amendContent(MainPromotionContentDto dto) {
        return true;
    }

    @Override
    public boolean amendIntro(MainPromotionIntroDto dto) {
        return true;
    }
}
