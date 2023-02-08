package com.promotion.handwriting.controller;

import com.promotion.handwriting.dto.ApiResponse;
import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.DeleteFileDto;
import com.promotion.handwriting.dto.IntroDto;
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
    public ApiResponse getImageList(
            @RequestParam("content_id") String id,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int count) {
        try {
            return ApiResponse.success(dataService.getCompressImageSrcByContentId(id, start, count));
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
            if (file != null) {
                String newImageFileName = fileService.saveIntroFile(file);
                dto.setImage(newImageFileName);
            } else {
                dto.setImage(UrlUtil.removeUrlPath(dto.getImage()));
            }
            return ApiResponse.success(dataService.amendIntro(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }

    @PostMapping("/content")
    public ApiResponse amendPromotionContent(@RequestBody ContentDto dto) {
        log.debug("POST : /data/content [input] >> " + dto);
        try {
            return ApiResponse.success(dataService.amendContent(dto));
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }

    @DeleteMapping("/content")
    public ApiResponse deletePromotionContent(@RequestBody ContentDto dto) {
        log.info("DELETE : /data/content [input] >> " + dto);

        try {
            long id = Long.parseLong(dto.getId());

            String resourcePath = dataService.getResourcePathOfAd(id);
            dataService.deleteAd(id);
            fileService.deleteDirectory(resourcePath);

            return ApiResponse.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }

    @PutMapping("/content")
    public ApiResponse createDetail(@RequestPart(name = "dto") ContentDto dto,
                                    @RequestPart(value = "image", required = false) List<MultipartFile> imageFiles) {

        log.info("PUT : /data/content [input] >> dto : " + dto);
        log.info("PUT : /data/content [input] >> imageFiles : " + imageFiles);

        try {
            ContentDto contentAd = dataService.createContentAd(dto);
            String resourcePath = dataService.getResourcePathOfAd(Long.parseLong(contentAd.getId()));
            if (imageFiles != null) {
                List<String> originImages = fileService.saveFiles(imageFiles, resourcePath);
                List<String> compressFiles = fileService.compressFiles(originImages, resourcePath);

                contentAd.setImages(compressFiles);
                contentAd.setOriginImages(originImages);
                for (int i = 0; i < originImages.size(); i++) {
                    String originImage = originImages.get(i);
                    String compressFile = compressFiles.get(i);
                    dataService.createImageAtAd(originImage, compressFile, Long.parseLong(contentAd.getId()));
                }
            }
            return ApiResponse.success(contentAd);
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
            String resourcePath = dataService.getResourcePathOfAd(Long.parseLong(id));

            for (MultipartFile file : imageFiles) {
                String filename = fileService.saveContentFile(file, resourcePath);
                String compressFile = fileService.compressFile(filename, resourcePath);
                dataService.createImageAtAd(filename, compressFile, Long.parseLong(id));
                log.info("file save : " + filename + ", + " + compressFile);
            }
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
            String resourcePath = dataService.getResourcePathOfAd(Long.parseLong(adId));
            List<String> fileList = fileUrls.getFiles().stream()
                    .map(UrlUtil::removeUrlPath)
                    .collect(Collectors.toList());

            List<String> deleteImages = dataService.deleteImages(fileList, Long.parseLong(adId));
            fileService.deleteFiles(deleteImages, resourcePath);

            return ApiResponse.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }
}
