package com.promotion.handwriting.controller;

import com.promotion.handwriting.dto.MainPromotionContentDto;
import com.promotion.handwriting.dto.MainPromotionIntroDto;
import com.promotion.handwriting.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/data")
@RestController
@RequiredArgsConstructor
@Slf4j
public class DataController {

    private final DataService dataService;
    @GetMapping("/content")
    Object getPromotionInformation() throws IOException {
        return dataService.readMainPromotionContentTextFile();
    }

    @GetMapping("/intro")
    Object getPromotionIntroInformation(){
        return dataService.readMainPromotionIntroTextFile();
    }

    @PostMapping("/content")
    Object amendPromotionContent(@RequestBody MainPromotionContentDto dto){
        log.debug("POST : /data/content [input] >> " + dto);
        return dataService.amendContent(dto);
    }

    @PostMapping("/intro")
    Object amendPromotionIntro(MainPromotionIntroDto dto){
        return dataService.amendIntro(dto);
    }
}
