package com.promotion.handwriting.controller;

import com.promotion.handwriting.dto.AdminDto;
import com.promotion.handwriting.dto.LoginDto;
import com.promotion.handwriting.security.JwtService;
import com.promotion.handwriting.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final JwtService jwtService;
    private final UserService loginService;

    @RequestMapping("/isAmend")
    Object isAmendPossible(HttpServletRequest request) {
        AdminDto adminDto = new AdminDto();

        String token = request.getParameter("Authorization");
        String username = token == null ? null : jwtService.getUserName(token);

        adminDto.setAmendAuthority(username != null);
        adminDto.setStatus(true);
        return adminDto;
    }

    @PostMapping("/join")
    Object join(@RequestBody LoginDto dto) {
        log.info("input data : " + dto);
        return loginService.join(dto.getId(), dto.getPw());
    }

    @PostMapping("/login")
    Object requestLogin(@RequestBody LoginDto dto, HttpServletResponse response) throws IOException {

        log.info("input data : " + dto);
        boolean isAdmin = loginService.login(dto.getId(), dto.getPw());

        if(isAdmin) {
            String jwtToken = jwtService.createJwt(dto.getId());
            response.setHeader("Authorization", "Bearer " + jwtToken);
            log.info("jwt token : " + response.getHeader("Authorization"));
        }
        AdminDto adminDto = new AdminDto();
        adminDto.setAmendAuthority(isAdmin);
        adminDto.setStatus(true);
        return adminDto;
    }
}
