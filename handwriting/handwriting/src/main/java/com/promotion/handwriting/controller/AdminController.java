package com.promotion.handwriting.controller;

import com.promotion.handwriting.dto.AdminDto;
import com.promotion.handwriting.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final LoginService loginService;

    @RequestMapping("/isAmend")
    Object isAmendPossible(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        AdminDto adminDto = new AdminDto();
        adminDto.setAmendAuthority(session != null);
        adminDto.setStatus(true);
        return adminDto;
    }

    @PostMapping("/login")
    Object requestLogin(HttpServletRequest request, @RequestParam String id, @RequestParam String pw) throws IOException {

        log.info("input data : [id : " + id + ", pw : " + pw + "]");
        boolean isAdmin = loginService.login(id, pw);

        request.getSession(isAdmin);
        AdminDto adminDto = new AdminDto();
        adminDto.setAmendAuthority(isAdmin);
        adminDto.setStatus(true);
        return adminDto;
    }
}
