package com.promotion.handwriting.controller;

import com.promotion.handwriting.dto.AdminDto;
import com.promotion.handwriting.dto.ApiResponse;
import com.promotion.handwriting.dto.LoginDto;
import com.promotion.handwriting.security.JwtService;
import com.promotion.handwriting.service.business.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.promotion.handwriting.enums.ApiResponseStatus.FAIL;

@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final JwtService jwtService;
    private final UserService loginService;

    @RequestMapping("/isAmend")
    ApiResponse isAmendPossible(HttpServletRequest request) {
        try {
            AdminDto adminDto = new AdminDto();

            String token = request.getParameter("Authorization");
            String username = token == null ? null : jwtService.getUserName(token);

            adminDto.setAmendAuthority(username != null);
            adminDto.setStatus(true);
            return ApiResponse.success(adminDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }

    @PostMapping("/join")
    ApiResponse join(@RequestBody LoginDto dto) {
        log.info("input data : " + dto);
        try {
            return ApiResponse.success(loginService.join(dto.getId(), dto.getPw()));
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }

    @PostMapping("/login")
    ApiResponse requestLogin(@RequestBody LoginDto dto, HttpServletResponse response) {

        log.info("input data : " + dto);

        try {
            boolean isAdmin = loginService.login(dto.getId(), dto.getPw());

            if(isAdmin) {
                String jwtToken = jwtService.createJwt(dto.getId());
                response.setHeader("Authorization", "Bearer " + jwtToken);
                log.info("jwt token : " + response.getHeader("Authorization"));
            }
            AdminDto adminDto = new AdminDto();
            adminDto.setAmendAuthority(isAdmin);
            adminDto.setStatus(true);
            return ApiResponse.success(adminDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(FAIL, null);
        }
    }
}
