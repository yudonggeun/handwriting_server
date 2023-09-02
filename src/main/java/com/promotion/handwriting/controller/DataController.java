package com.promotion.handwriting.controller;

import com.promotion.handwriting.dto.request.ImageDeleteRequest;
import com.promotion.handwriting.dto.IntroDto;
import com.promotion.handwriting.dto.request.ContentChangeRequest;
import com.promotion.handwriting.dto.request.CreateContentRequest;
import com.promotion.handwriting.dto.request.DeleteContentRequest;
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
import java.util.stream.Collectors;

import static com.promotion.handwriting.dto.response.ApiResponse.*;

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
                                   @RequestPart(name = "dto") IntroDto dto) throws IOException {
        dataService.updateIntro(dto, file);
        return success(null);
    }

    @PostMapping("/content")
    public ApiResponse updateContent(@RequestBody ContentChangeRequest request) {
        dataService.updateContent(request);
        return success(null);
    }

    @DeleteMapping("/content")
    public ApiResponse deleteContent(@RequestBody DeleteContentRequest request) throws IOException {
        dataService.deleteAd(request.getContentId());
        return success(null);
    }

    @PutMapping("/content")
    public ApiResponse createContent(@RequestPart(name = "dto") CreateContentRequest request,
                                     @RequestPart(value = "image", required = false) List<MultipartFile> images) throws IOException {
        return success(dataService.addContentAd(request, images));
    }

    @PutMapping("/detail/{id}")
    public ApiResponse addDetailImages(@PathVariable("id") String id,
                                       @RequestPart(name = "image", required = false) List<MultipartFile> imageFiles) throws IOException {
        for (MultipartFile file : imageFiles)
            dataService.addImage(file, Long.parseLong(id));
        return success(null);
    }

    @DeleteMapping("/detail/{id}")
    public ApiResponse deleteDetailImages(@PathVariable("id") String adId,
                                          @RequestBody ImageDeleteRequest fileUrls) {
        //url 데이터 가공한다. TODO front 에서 정제하면 좋다.
        List<String> fileList = fileUrls.getFiles().stream()
                .map(url -> url.substring(url.lastIndexOf("/") + 1))
                .collect(Collectors.toList());

        dataService.deleteImages(fileList, Long.parseLong(adId));

        return success(null);
    }
}
