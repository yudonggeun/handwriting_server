package com.promotion.handwriting.controller;

import com.promotion.handwriting.dto.request.LoginRequest;
import com.promotion.handwriting.dto.response.ApiResponse;
import com.promotion.handwriting.security.JwtService;
import com.promotion.handwriting.service.business.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.promotion.handwriting.dto.response.ApiResponse.success;

@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final JwtService jwtService;
    private final UserService loginService;

    @PostMapping("/join")
    public ApiResponse join(@RequestBody LoginRequest dto) {
        loginService.join(dto.getId(), dto.getPw());
        return success(null);
    }

    @GetMapping("/login")
    public ApiResponse oauthLogin(HttpServletRequest request, HttpServletResponse response, @SessionAttribute("userId") String userId) {
        request.getSession().invalidate();
        response.setHeader("Authorization", "Bearer " + jwtService.createJwt(userId));
        return success(null);
    }

    @PostMapping("/login")
    public ApiResponse requestLogin(@RequestBody LoginRequest dto, HttpServletResponse response) throws IOException {
        loginService.login(dto.getId(), dto.getPw());
        response.setHeader("Authorization", "Bearer " + jwtService.createJwt(dto.getId()));
        return success(null);
    }
}
