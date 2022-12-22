package com.promotion.handwriting.controller;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.DeleteFileDto;
import com.promotion.handwriting.dto.IntroDto;
import com.promotion.handwriting.service.DataService;
import com.promotion.handwriting.service.FileService;
import com.promotion.handwriting.util.UrlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/data")
@RestController
@RequiredArgsConstructor
@Slf4j
public class DataController {

    private final DataService dataService;
    private final FileService fileService;

    @GetMapping("/content")
    public Object getPromotionInformation() throws IOException {
        return dataService.getContentDtos();
    }

    @GetMapping("/intro")
    public Object getPromotionIntroInformation() {
        return dataService.getIntroDto();
    }

    @PostMapping("/content")
    public Object amendPromotionContent(@RequestBody ContentDto dto) {
        log.debug("POST : /data/content [input] >> " + dto);
        return dataService.amendContent(dto);
    }

    @PostMapping("/intro")
    public Object amendPromotionIntro(@RequestPart(name = "file", required = false) MultipartFile file,
                               @RequestPart(name = "dto") IntroDto dto) throws IOException {

        log.info(dto.toString());
        log.info("file : " + (file == null ? "null" : file.getOriginalFilename()));

        dto.setImage(UrlUtil.removeUrlPath(dto.getImage()));
        boolean success = dataService.amendIntro(dto);
        if (file != null) {
            fileService.saveIntroFile(file);
        }
        return success;
    }

    @PutMapping("/detail/{id}")
    public Object addDetailImages(@PathVariable("id") String id,
                           @RequestPart(name = "image", required = false) List<MultipartFile> files) throws IOException {
        log.info("append images at " + id);

        for (MultipartFile file : files) {
            String filename = fileService.saveFile(file, Long.parseLong(id));
            log.info("file save : " + filename);
        }

        return true;
    }

    @DeleteMapping("/detail/{id}")
    public Object deleteDetailImages(@PathVariable("id") String id,
                           @RequestBody DeleteFileDto fileUrls) throws IOException {
        log.info("delete images at " + id);

        Set<String> fileSet = fileUrls.getFiles().stream()
                .map(UrlUtil::removeUrlPath)
                .collect(Collectors.toSet());
        fileService.deleteFiles(fileSet, Long.parseLong(id));

        return true;
    }

    @GetMapping("/content/image")
    public Object getImageList(
            @RequestParam("content_id") String id,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int count) throws IOException {
        return dataService.getImageSrcByContentId(id, start, count);
    }
}
