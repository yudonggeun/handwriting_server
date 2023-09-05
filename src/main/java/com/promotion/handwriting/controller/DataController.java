package com.promotion.handwriting.controller;

import com.promotion.handwriting.dto.request.*;
import com.promotion.handwriting.dto.response.ApiResponse;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.service.business.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.promotion.handwriting.dto.response.ApiResponse.success;

@RequestMapping("/data")
@RestController
@RequiredArgsConstructor
@Slf4j
public class DataController {

    private final DataService dataService;

    @GetMapping("/content")
    public ApiResponse getPromotionInformation(@PageableDefault Pageable pageable) throws IOException {
        return success(dataService.getContentDtos(AdType.CONTENT, pageable));
    }

    @Deprecated
    @GetMapping("/intro")
    public ApiResponse getPromotionIntroInformation() {
        return success(dataService.mainPageData());
    }

    @GetMapping("/content/image")
    public ApiResponse getImageList(@RequestParam("content_id") String contentId, @PageableDefault Pageable pageable) {
        return success(dataService.getImageUrls(contentId, pageable));
    }

    @PostMapping("/intro")
    public ApiResponse updateIntro(@RequestPart(name = "file", required = false) MultipartFile file,
                                   @RequestPart(name = "dto") ChangeMainPageRequest request) throws IOException {
        dataService.updateMainPage(request, file);
        return success(null);
    }

    @PostMapping("/content")
    public ApiResponse updateContent(@RequestBody ChangeContentRequest request) {
        dataService.updateContent(request);
        return success(null);
    }

    @DeleteMapping("/content")
    public ApiResponse deleteContent(@RequestBody DeleteContentRequest request) throws IOException {
        dataService.deleteContent(request.getContentId());
        return success(null);
    }

    @PutMapping("/content")
    public ApiResponse createContent(@RequestPart(name = "dto") CreateContentRequest request,
                                     @RequestPart(value = "image", required = false) List<MultipartFile> images) throws IOException {
        return success(dataService.newContent(request, images));
    }

    @PutMapping("/detail/{id}")
    public ApiResponse addDetailImages(@PathVariable("id") String id,
                                       @RequestPart(name = "image", required = false) List<MultipartFile> imageFiles) throws IOException {
        for (MultipartFile file : imageFiles)
            dataService.newImage(file, Long.parseLong(id));
        return success(null);
    }

    @DeleteMapping("/detail")
    public ApiResponse deleteDetailImages(@RequestBody DeleteImageRequest request) {
        dataService.deleteImages(request.getImageIds(), request.getContentId());
        return success(null);
    }
}
