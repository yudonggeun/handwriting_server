package com.promotion.handwriting.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LoginImpl implements LoginService {

    @Override
    public boolean login(String id, String pw) throws IOException {

        ClassPathResource loginPath = new ClassPathResource("/text/login.txt");
        Path path = Paths.get(loginPath.getURI());
        List<String> info = Files.readAllLines(path);
        Map<String, String> match = new HashMap<>();
        for (String line : info) {
            String[] split = line.split(":");
            log.debug("login.txt : " + Arrays.toString(split));
            if(split.length != 2){
                log.info("로그인 파일의 정보를 수정하세요.");
                return false;
            }
            match.put(split[0], split[1]);
        }

        return match.get("id").equals(id) && match.get("pw").equals(pw);
    }
}
