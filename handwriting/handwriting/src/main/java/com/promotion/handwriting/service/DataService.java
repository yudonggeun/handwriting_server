package com.promotion.handwriting.service;

import com.promotion.handwriting.dto.MainPromotionContentDto;
import com.promotion.handwriting.dto.MainPromotionIntroDto;

import java.io.IOException;
import java.util.List;

public interface DataService {
    List<String> getImageSrcByContentId(String id, int start, int count) throws IOException;

    List<MainPromotionContentDto> readMainPromotionContentTextFile() throws IOException;

    MainPromotionIntroDto readMainPromotionIntroTextFile();

    boolean amendContent(MainPromotionContentDto dto);

    boolean amendIntro(MainPromotionIntroDto dto);
}
