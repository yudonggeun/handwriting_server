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

    @PostMapping("/intro")
    public Object amendPromotionIntro(@RequestPart(name = "file", required = false) MultipartFile file,
                                      @RequestPart(name = "dto") IntroDto dto) throws IOException {
        log.info(dto.toString());
        log.info("file : " + (file == null ? "null" : file.getOriginalFilename()));

        dto.setImage(UrlUtil.removeUrlPath(dto.getImage()));
        if (file != null) {
            String newImageFileName = fileService.saveIntroFile(file);
            dto.setImage(newImageFileName);
        }
        return dataService.amendIntro(dto);
    }

    @PostMapping("/content")
    public Object amendPromotionContent(@RequestBody ContentDto dto) {
        log.debug("POST : /data/content [input] >> " + dto);
        return dataService.amendContent(dto);
    }

    @PutMapping("/content")
    public Object createDetail(@RequestPart(name = "dto") ContentDto dto,
                               @RequestPart("image") List<MultipartFile> imageFiles) throws IOException {

        log.info("PUT : /data/content [input] >> dto : " + dto);
        log.info("PUT : /data/content [input] >> imageFiles" + imageFiles);

        if (dto.getImages().size() != imageFiles.size()) {
            dto.setImages(imageFiles.stream()
                    .map(MultipartFile::getOriginalFilename)
                    .collect(Collectors.toList())
            );
        }

        ContentDto contentAd = dataService.createContentAd(dto);
        fileService.saveFiles(imageFiles, Long.parseLong(contentAd.getId()));

        return true;
    }

    @PutMapping("/detail/{id}")
    public Object addDetailImages(@PathVariable("id") String id,
                                  @RequestPart(name = "image", required = false) List<MultipartFile> imageFiles) throws IOException {
        log.info("append images at " + id);

        for (MultipartFile file : imageFiles) {
            String filename = fileService.saveFile(file, Long.parseLong(id));
            dataService.createImageAtAd(filename, Long.parseLong(id));
            log.info("file save : " + filename);
        }

        return true;
    }

    @DeleteMapping("/detail/{id}")
    public Object deleteDetailImages(@PathVariable("id") String adId,
                                     @RequestBody DeleteFileDto fileUrls) throws IOException {
        log.info("delete images at " + adId);

        List<String> fileList = fileUrls.getFiles().stream()
                .map(UrlUtil::removeUrlPath)
                .collect(Collectors.toList());

        dataService.deleteImages(fileList, Long.parseLong(adId));
        fileService.deleteFiles(fileList, Long.parseLong(adId));

        return true;
    }

    @GetMapping("/content/image")
    public Object getImageList(
            @RequestParam("content_id") String id,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int count) throws IOException {
        return dataService.getImageSrcByContentId(id);
    }
}
