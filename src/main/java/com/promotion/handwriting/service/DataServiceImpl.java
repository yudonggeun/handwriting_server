package com.promotion.handwriting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.IntroDto;
import com.promotion.handwriting.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final ResourceLoader loader;

    private final String imageIntroUrlPath = "/api/image/intro/";
    private final String imageContentUrlPath = "/api/image/content/";
    private final String textContentUrlPath = "/api/text/content/";
    private final String textIntroUrlPath = "/api/text/intro/";

    private final String imageIntroPath = "/image/intro/";
    private final String imageContentPath = "/image/content/";
    private final String textContentPath = "/text/content/";
    private final String textIntroPath = "/text/intro/";

    @Override
    public List<String> getImageSrcByContentId(String id, int start, int count) throws IOException {
        BufferedReader imageReader = new BufferedReader(new InputStreamReader(
                loader.getResource(FileUtil.getFileResourcePath() + imageContentPath + id).getInputStream()));
        List<String> allImages = imageReader.lines()
                .filter(imageFilename -> imageFilename.endsWith("jpg") || imageFilename.endsWith("png"))
                .map(imageFilename -> imageContentUrlPath + id + "/" + imageFilename)
                .collect(Collectors.toList());
        int endPoint = Math.min(allImages.size(), start + count);
        return allImages.subList(start, endPoint);
    }

    @Override
    public List<ContentDto> getContentDtos() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(loader.getResource(FileUtil.getFileResourcePath() + textContentPath).getInputStream()));
            return br.lines().map(fileName -> {
                        try {

                            InputStream file = loader.getResource(FileUtil.getFileResourcePath() + textContentPath + fileName).getInputStream();
                            ContentDto dto = new ObjectMapper().readValue(new InputStreamReader(file), ContentDto.class);

                            getImageSrcByContentId(dto.getId(), 0, 8)
                                    .forEach(dto::addImage);

                            log.debug("image list size : " + dto.getImages().size());
                            log.debug(dto.getId() + ".json 조회 : " + dto);
                            log.debug("image list size : " + dto.getImages().size());

                            return dto;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IntroDto getIntroDto() {
        try {
            Resource resource = loader.getResource(FileUtil.getFileResourcePath() + textIntroPath + "intro.json");
            InputStream file = resource.getInputStream();
            IntroDto result = new ObjectMapper().readValue(file, IntroDto.class);
            result.setImage(imageIntroUrlPath + result.getImage());
            log.debug("intro.json" + " 조회 : " + result);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean amendContent(ContentDto dto) {
        try {
            String target = String.format(textContentPath + "%s.json", dto.getId());
            Resource resource = loader.getResource(FileUtil.getFileResourcePath() + target);
            File file = resource.getFile();

            ObjectMapper mapper = new ObjectMapper();
            dto.getImages().clear();//image 의존성 삭제
            mapper.writeValue(file, dto);

            log.info("PATH : " + target);
            log.info(dto.getId() + ".json 수정완료");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean amendIntro(IntroDto dto) {
        try {
            Resource resource = loader.getResource(FileUtil.getFileResourcePath() + textIntroPath + "intro.json");
            File file = resource.getFile();
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();
            // convert map to JSON file
            mapper.writeValue(file, dto);
            log.info("intro.json 수정완료");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
