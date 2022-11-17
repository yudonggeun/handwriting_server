package com.promotion.handwriting.controller;

import com.promotion.handwriting.dto.MainPromotionContentDto;
import com.promotion.handwriting.dto.MainPromotionIntroDto;
import com.promotion.handwriting.service.DataService;
import com.promotion.handwriting.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/data")
@RestController
@RequiredArgsConstructor
@Slf4j
public class DataController {

    private final DataService dataService;
    private final FileService fileService;

    @GetMapping("/content")
    Object getPromotionInformation() throws IOException {
        return dataService.readMainPromotionContentTextFile();
    }

    @GetMapping("/intro")
    Object getPromotionIntroInformation() {
        return dataService.readMainPromotionIntroTextFile();
    }

    @PostMapping("/content")
    Object amendPromotionContent(@RequestBody MainPromotionContentDto dto) {
        log.debug("POST : /data/content [input] >> " + dto);
        return dataService.amendContent(dto);
    }

    @PostMapping("/intro")
    Object amendPromotionIntro(@RequestPart(name = "file", required = false) MultipartFile file,
                               @RequestPart(name = "dto") MainPromotionIntroDto dto) throws IOException {

        log.info(dto.toString());
        log.info(file.getOriginalFilename());

        boolean success = dataService.amendIntro(dto);
        if(success){
            fileService.saveFile(file);
        }
        return success;
    }
}
