package com.promotion.handwriting.controller;

import com.promotion.handwriting.dto.AdminDto;
import com.promotion.handwriting.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping("/admin")
@RestController
public class AdminController {

    LoginService loginService;

    @RequestMapping("/isAmend")
    Object isAmendPossible(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        AdminDto adminDto = new AdminDto();
        adminDto.setAdmin(session != null);
        adminDto.setStatus(true);
        return adminDto;
    }

    @PostMapping("/login")
    Object requestLogin(HttpServletRequest request, @RequestBody String id, @RequestBody String pw) {

        boolean isAdmin = loginService.login(id, pw);

        request.getSession(isAdmin);
        AdminDto adminDto = new AdminDto();
        adminDto.setAdmin(isAdmin);
        adminDto.setStatus(true);
        return adminDto;
    }
}
