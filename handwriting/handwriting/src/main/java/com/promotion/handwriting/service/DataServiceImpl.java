package com.promotion.handwriting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promotion.handwriting.dto.MainPromotionContentDto;
import com.promotion.handwriting.dto.MainPromotionIntroDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class DataServiceImpl implements DataService {
    @Override
    public List<MainPromotionContentDto> readMainPromotionContentTextFile() {
        List<MainPromotionContentDto> result = new LinkedList<>();
        try {
            ClassPathResource resource = new ClassPathResource("/text/content/");
            String imagePath = "/static/image/content/";

            File dir = resource.getFile();
            File[] files = dir.listFiles((dir1, name) -> name.endsWith("json"));

            if(files != null) {
                for (File file : files) {
                    MainPromotionContentDto dto = new ObjectMapper().readValue(file, MainPromotionContentDto.class);

                    File imageDir = new ClassPathResource(imagePath + dto.getId()).getFile();
                    File[] images = imageDir.listFiles((f, name) -> name.endsWith("jpg") || name.endsWith("png"));

                    if(images == null) continue;
                    int repeat = Math.min(images.length, 8);
                    for (int i = 0; i < repeat; i++) {
                        File image = images[i];
                        dto.addImage("/image/content/" + dto.getId() + "/" + image.getName());
                    }
                    log.debug(file.getName() + " 조회 : " + dto);
                    result.add(dto);
                }
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MainPromotionIntroDto readMainPromotionIntroTextFile() {
        return null;
    }

    @Override
    public boolean amendContent(MainPromotionContentDto dto) {
        try {
            String target = String.format("/text/content/%s.json", dto.getId());
            log.info("PATH : " + target);
            ClassPathResource resource = new ClassPathResource(target);
            File file = resource.getFile();
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();
            // convert map to JSON file
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
        return true;
    }
}
