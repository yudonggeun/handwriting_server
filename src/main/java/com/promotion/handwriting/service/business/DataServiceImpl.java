package com.promotion.handwriting.service.business;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.IntroDto;
import com.promotion.handwriting.dto.SimpleContentDto;
import com.promotion.handwriting.dto.file.FileToken;
import com.promotion.handwriting.dto.file.LocalFileToken;
import com.promotion.handwriting.dto.image.ImageDto;
import com.promotion.handwriting.dto.image.MultipartImageDto;
import com.promotion.handwriting.dto.image.UrlImageDto;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.database.AdRepository;
import com.promotion.handwriting.repository.database.ImageRepository;
import com.promotion.handwriting.repository.file.FileRepository;
import com.promotion.handwriting.util.ImageUtil;
import com.promotion.handwriting.util.UrlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final AdRepository adRepository;
    private final ImageRepository imageRepository;
    private final FileRepository fileRepository;

    @Override
    public ContentDto addContentAd(ContentDto dto) throws IOException {
        Ad ad = Ad.createAd(AdType.CONTENT, dto.getTitle(), dto.getDescription(), "/content/" + UUID.randomUUID());

        for (ImageDto imageDto : dto.getImages()) {
            if (imageDto instanceof MultipartImageDto) {
                MultipartImageDto multipartImageDto = (MultipartImageDto) imageDto;
                MultipartFile file = multipartImageDto.getMultipartFile();

                //파일을 저장한다.
                String originalFilename = file.getOriginalFilename();
                String compressFilename = ImageUtil.compressImageName(originalFilename);
                String resourcePath = ad.getResourcePath();
                FileToken fileToken = LocalFileToken.save(file.getInputStream(), resourcePath, originalFilename);

                if (!fileRepository.save(fileToken) || !fileRepository.compressAndSave(originalFilename, compressFilename, resourcePath)) {
                    throw new IllegalArgumentException("파일 저장 실패");
                }
                //db에 image에 대한 데이터를 생성한다.
                Image image = Image.builder()
                        .priority(Integer.MAX_VALUE)
                        .imageName(originalFilename)
                        .compressImageName(compressFilename)
                        .build();
                ad.addImage(image);
            }
        }
        adRepository.save(ad);
        return ad.contentDto();
    }

    @Override
    public String addImageAtAd(MultipartFile imageFile, long adId) {
        try {
            Ad ad = adRepository.findById(adId).orElseThrow();
            //파일을 저장한다.
            String originalFilename = imageFile.getOriginalFilename();
            String compressFilename = ImageUtil.compressImageName(originalFilename);
            FileToken originToken = new LocalFileToken(imageFile.getInputStream(), ad.getResourcePath(), originalFilename);
            if (!fileRepository.save(originToken) || !fileRepository.compressAndSave(originalFilename, compressFilename, ad.getResourcePath())) {
                throw new IllegalArgumentException("파일 저장 실패");
            }
            //db에 image에 대한 데이터를 생성한다.
            Image image = Image.builder()
                    .priority(Integer.MAX_VALUE)
                    .imageName(originalFilename)
                    .compressImageName(compressFilename)
                    .build();

            ad.addImage(image);
            return originalFilename;
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            log.error("multipart getInputStream() 에러 발생 At DataService.createImageAtAd");
            return null;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ContentDto> getContentDtos(AdType type) {
        return adRepository.findByType(type).stream()
                .map(Ad::contentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UrlImageDto> getImageUrlAtContent(String contentId, int start, int end) {
        Ad content = adRepository.findAdWithImagesById(Long.parseLong(contentId));
        return content.getImages()
                .stream()
                .map(image -> {
                    String originUrl = UrlUtil.getImageUrl(content.getResourcePath(), image.getImageName());
                    String compressUrl = UrlUtil.getImageUrl(content.getResourcePath(), image.getCompressImageName());
                    return UrlImageDto.make(originUrl, compressUrl);
                }).collect(Collectors.toList());
    }

    @Override
    public boolean updateContent(SimpleContentDto dto) {
        Ad ad = adRepository.getReferenceById(dto.getId());
        ad.setDetail(dto.getDescription());
        ad.setTitle(dto.getTitle());
        return true;
    }

    @Override
    public boolean updateIntro(IntroDto dto, MultipartFile file) throws IOException {
        Ad intro = adRepository.findByType(AdType.INTRO).get(0);

        StringBuilder sb = new StringBuilder();
        //TODO css로 해결하는것이 경제적
        dto.getComments().forEach(comment -> sb.append(comment).append(IntroDto.separate));
        sb.deleteCharAt(sb.length() - 1);

        intro.setDetail(sb.toString());

        Image introImage = intro.getImages().get(0);
        if (!(file == null)) {
            String resourcePath = intro.getResourcePath();
            String originalFilename = file.getOriginalFilename();
            String compressFilename = ImageUtil.compressImageName(originalFilename);
            //파일 삭제
            fileRepository.delete(LocalFileToken.delete(resourcePath, introImage.getCompressImageName()));
            fileRepository.delete(LocalFileToken.delete(resourcePath, introImage.getImageName()));
            //파일 저장
            fileRepository.save(LocalFileToken.save(file.getInputStream(), resourcePath, originalFilename));
            fileRepository.compressAndSave(originalFilename, compressFilename, resourcePath);

            introImage.setImageName(originalFilename);
            introImage.setCompressImageName(compressFilename);
        }
        return true;
    }

    @Override
    public void deleteAd(long id) {

        Ad content = adRepository.findAdWithImagesById(id);
        FileToken token = LocalFileToken.deleteDirectory(content.getResourcePath());
        fileRepository.deleteDirectory(token);
        imageRepository.deleteAllByAd(id);
        adRepository.deleteIgnoreReferenceById(id);
    }

    @Override
    public void deleteImages(List<String> fileNames, long adId) {
        Ad ad = adRepository.findAdWithImagesById(adId);

        List<Image> deleteImage = ad.getImages().stream()
                .filter(image -> fileNames.contains(image.getImageName()) || fileNames.contains(image.getCompressImageName()))
                .collect(Collectors.toList());

        //파일 삭제
        deleteImage.forEach(image -> {
            fileRepository.delete(LocalFileToken.delete(ad.getResourcePath(), image.getImageName()));
            fileRepository.delete(LocalFileToken.delete(ad.getResourcePath(), image.getCompressImageName()));
        });
        //데이터 삭제
        imageRepository.deleteAllInBatch(deleteImage);
    }
}
