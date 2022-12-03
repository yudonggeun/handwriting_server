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
import java.util.List;

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
        if (success) {
            fileService.saveFile(file, "image/intro");
        }
        return success;
    }

    @PutMapping("/detail/{id}")
    Object addDetailImages(@PathVariable("id") String id,
                           @RequestPart(name = "image", required = false) List<MultipartFile> files) throws IOException {
        log.info("append images at " + id);

        for (MultipartFile file : files) {
            String filename = fileService.saveFile(file, "image/content/" + id);
            log.info("file save : " + filename);
        }

        return true;
    }

    @DeleteMapping("/detail/{id}")
    Object deleteDetailImages(@PathVariable("id") String id,
                           @RequestBody List<String> files) throws IOException {
        log.info("append images at " + id);

        for (String file : files) {
            String filename = fileService.deleteFile(file, "image/content/" + id);
            log.info("file save : " + filename);
        }

        return true;
    }

    @GetMapping("/content/image")
    Object getImageList(
            @RequestParam("content_id") String id,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int count) throws IOException {
        return dataService.getImageSrcByContentId(id, start, count);
    }
}
