package com.promotion.handwriting.controller;

import com.promotion.handwriting.dto.MainPromotionContentDto;
import com.promotion.handwriting.dto.MainPromotionIntroDto;
import com.promotion.handwriting.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/data")
@RestController
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;
    @GetMapping("/content")
    Object getPromotionInformation(){
        return dataService.readMainPromotionContentTextFile();
    }

    @GetMapping("/intro")
    Object getPromotionIntroInformation(){
        return dataService.readMainPromotionIntroTextFile();
    }

    @PostMapping("/content")
    Object amendPromotionContent(MainPromotionContentDto dto){
        return dataService.amendContent(dto);
    }

    @PostMapping("/intro")
    Object amendPromotionIntro(MainPromotionIntroDto dto){
        return dataService.amendIntro(dto);
    }
}
