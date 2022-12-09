package com.promotion.handwriting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promotion.handwriting.dto.MainPromotionContentDto;
import com.promotion.handwriting.dto.MainPromotionIntroDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataServiceImpl implements DataService {

    @Override
    public List<String> getImageSrcByContentId(String id, int start, int count) throws IOException {
        BufferedReader imageReader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("/static/image/content/" + id).getInputStream()));
        List<String> allImages = imageReader.lines()
                .filter(name -> name.endsWith("jpg") || name.endsWith("png"))
                .map(name -> "/image/content/" + id + "/" + name)
                .collect(Collectors.toList());
        int endPoint = Math.min(allImages.size(), start + count);
        return allImages.subList(start, endPoint);
    }

    @Override
    public List<MainPromotionContentDto> readMainPromotionContentTextFile() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("/text/content/").getInputStream()));
            return br.lines().map(fileName -> {
                        try {
                            InputStream file = new ClassPathResource("/text/content/" + fileName).getInputStream();
                            MainPromotionContentDto dto = new ObjectMapper().readValue(new InputStreamReader(file), MainPromotionContentDto.class);

                            getImageSrcByContentId(dto.getId(), 0, 8).forEach(image ->
                                    dto.addImage("/image/content/" + dto.getId() + "/" + image)
                            );
                            log.debug("image list size : " + dto.getImages().size());
                            log.debug(dto.getId() + ".json 조회 : " + dto);
                            log.debug("image directory : " + "/static/image/content/" + dto.getId());
                            log.debug("image list size : " + dto.getImages().size());
                            log.debug("dto hash : " + dto.hashCode());
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
    public MainPromotionIntroDto readMainPromotionIntroTextFile() {
        try {
            ClassPathResource resource = new ClassPathResource("/text/intro/intro.json");
            InputStream file = resource.getInputStream();
            MainPromotionIntroDto result = new ObjectMapper().readValue(file, MainPromotionIntroDto.class);
            result.setImage("/image/intro/" + result.getImage());
            log.debug("intro.json" + " 조회 : " + result);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean amendContent(MainPromotionContentDto dto) {
        try {
            String target = String.format("/text/content/%s.json", dto.getId());
            log.info("PATH : " + target);
            ClassPathResource resource = new ClassPathResource(target);
            File file = resource.getFile();

            ObjectMapper mapper = new ObjectMapper();
            dto.getImages().clear();//image 의존성 삭제
            mapper.writeValue(file, dto);
            log.info(dto.getId() + ".json 수정완료");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean amendIntro(MainPromotionIntroDto dto) {
        try {
            ClassPathResource resource = new ClassPathResource("/text/intro/intro.json");
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
