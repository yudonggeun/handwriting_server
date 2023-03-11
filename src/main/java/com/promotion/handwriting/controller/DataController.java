package com.promotion.handwriting.controller;

import com.promotion.handwriting.dto.*;
import com.promotion.handwriting.dto.http.ApiResponse;
import com.promotion.handwriting.dto.image.ImageDto;
import com.promotion.handwriting.dto.image.MultipartImageDto;
import com.promotion.handwriting.service.business.DataService;
import com.promotion.handwriting.service.file.FileService;
import com.promotion.handwriting.util.UrlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.promotion.handwriting.enums.ApiResponseStatus.*;

@RequestMapping("/data")
@RestController
@RequiredArgsConstructor
@Slf4j
public class DataController {
    private final DataService dataService;

    private final FileService fileService;

    @GetMapping("/content")
    public ApiResponse getPromotionInformation() {
        try {
            return ApiResponse.success(dataService.getContentDtos());
        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }

    @GetMapping("/intro")
    public ApiResponse getPromotionIntroInformation() {
        try {
            return ApiResponse.success(dataService.getIntroDto());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }

    @GetMapping("/content/image")
    public ApiResponse getImageList(//TODO front 수정
            @RequestParam("content_id") String id,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int count) {
        try {
            return ApiResponse.success(dataService.getImageUrlAtContent(id, start, count));
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }

    @PostMapping("/intro")
    public ApiResponse amendPromotionIntro(@RequestPart(name = "file", required = false) MultipartFile file,
                                           @RequestPart(name = "dto") IntroDto dto) {
        log.info(dto.toString());
        log.info("file : " + (file == null ? "null" : file.getOriginalFilename()));

        try {
            return ApiResponse.success(dataService.updateIntro(dto, file));
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }

    @PostMapping("/content")
    public ApiResponse amendPromotionContent(@RequestBody SimpleContentDto dto) {
        log.debug("POST : /data/content [input] >> " + dto);
        try {
            return ApiResponse.success(dataService.updateContent(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }

    @DeleteMapping("/content")
    public ApiResponse deletePromotionContent(@RequestBody ContentDto dto) {
        log.info("DELETE : /data/content [input] >> " + dto);

        try {
            dataService.deleteAd(Long.parseLong(dto.getId()));
            return ApiResponse.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }

    @PutMapping("/content")
    public ApiResponse createContent(@RequestPart(name = "dto") ContentDto dto,
                                    @RequestPart(value = "image", required = false) List<MultipartFile> imageFiles) {

        log.info("PUT : /data/content [input] >> dto : " + dto);
        log.info("PUT : /data/content [input] >> imageFiles : " + imageFiles);

        try {
            if(imageFiles != null) {
                List<ImageDto> imageDtos = imageFiles.stream()
                        .map(MultipartImageDto::make)
                        .collect(Collectors.toList());
                dto.setImages(imageDtos);
            }

            return ApiResponse.success(dataService.addContentAd(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }

    @PutMapping("/detail/{id}")
    public ApiResponse addDetailImages(@PathVariable("id") String id,
                                       @RequestPart(name = "image", required = false) List<MultipartFile> imageFiles) {
        log.info("append images at " + id);

        try {
            imageFiles.forEach(file -> dataService.addImageAtAd(file, Long.parseLong(id)));
            return ApiResponse.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }

    @DeleteMapping("/detail/{id}")
    public ApiResponse deleteDetailImages(@PathVariable("id") String adId,
                                          @RequestBody DeleteFileDto fileUrls) {
        log.info("delete images at " + adId);

        try {
            //url 데이터 가공한다. TODO front 에서 정제하면 좋다.
            List<String> fileList = fileUrls.getFiles().stream()
                    .map(UrlUtil::removeUrlPath)
                    .collect(Collectors.toList());

            dataService.deleteImages(fileList, Long.parseLong(adId));

            return ApiResponse.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }
}
