package com.promotion.handwriting.controller;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.DeleteFileDto;
import com.promotion.handwriting.dto.IntroDto;
import com.promotion.handwriting.dto.SimpleContentDto;
import com.promotion.handwriting.dto.request.CreateContentRequest;
import com.promotion.handwriting.dto.response.ApiResponse;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.service.business.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/content")
    public ApiResponse getPromotionInformation(@PageableDefault(size = 5) Pageable pageable) throws IOException {
        return ApiResponse.success(dataService.getContentDtos(AdType.CONTENT, pageable));
    }

    @Deprecated
    @GetMapping("/intro")
    public ApiResponse getPromotionIntroInformation() throws IOException {
        IntroDto introDto = dataService.getContentDtos(AdType.INTRO, PageRequest.of(0, 1)).getContent().get(0).introDto();
        return ApiResponse.success(introDto);
    }

    @GetMapping("/content/image")
    public ApiResponse getImageList(//TODO front 수정
                                    @RequestParam("content_id") String contentId,
                                    @RequestParam(defaultValue = "0") int start,
                                    @RequestParam(defaultValue = "10") int count) {
        return ApiResponse.success(dataService.getImageUrlAtContent(contentId, start, count));
    }

    @PostMapping("/intro")
    public ApiResponse updateIntro(@RequestPart(name = "file", required = false) MultipartFile file,
                                   @RequestPart(name = "dto") IntroDto dto) throws IOException {
        log.info(dto.toString());
        log.info("file : " + (file == null ? "null" : file.getOriginalFilename()));
        return ApiResponse.success(dataService.updateIntro(dto, file));
    }

    @PostMapping("/content")
    public ApiResponse updateContent(@RequestBody SimpleContentDto dto) {
        log.debug("POST : /data/content [input] >> " + dto);
        return ApiResponse.success(dataService.updateContent(dto));
    }

    @DeleteMapping("/content")
    public ApiResponse deleteContent(@RequestBody ContentDto dto) throws IOException {
        log.info("DELETE : /data/content [input] >> " + dto);
        dataService.deleteAd(Long.parseLong(dto.getId()));
        return ApiResponse.success(true);
    }

    @PutMapping("/content")
    public ApiResponse createContent(@RequestPart(name = "dto") CreateContentRequest request,
                                     @RequestPart(value = "image", required = false) List<MultipartFile> images) throws IOException {

        log.info("PUT : /data/content [input] >> dto : " + request);
        log.info("PUT : /data/content [input] >> imageFiles : " + images);
        return ApiResponse.success(dataService.addContentAd(request, images));
    }

    @PutMapping("/detail/{id}")
    public ApiResponse addDetailImages(@PathVariable("id") String id,
                                       @RequestPart(name = "image", required = false) List<MultipartFile> imageFiles) throws IOException {
        log.info("append images at " + id);

        for (MultipartFile file : imageFiles) {
            dataService.addImage(file, Long.parseLong(id));
        }
        return ApiResponse.success(true);
    }

    @DeleteMapping("/detail/{id}")
    public ApiResponse deleteDetailImages(@PathVariable("id") String adId,
                                          @RequestBody DeleteFileDto fileUrls) {
        log.info("delete images at " + adId);

        //url 데이터 가공한다. TODO front 에서 정제하면 좋다.
        List<String> fileList = fileUrls.getFiles().stream()
                .map(url -> url.substring(url.lastIndexOf("/") + 1))
                .collect(Collectors.toList());

        dataService.deleteImages(fileList, Long.parseLong(adId));

        return ApiResponse.success(true);
    }
}
