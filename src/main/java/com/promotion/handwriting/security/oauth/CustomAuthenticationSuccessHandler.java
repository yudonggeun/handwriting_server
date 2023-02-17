package com.promotion.handwriting.security.oauth;

import com.promotion.handwriting.security.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * spring security Authentication 인증 성공시 후처리 클래스
 * 세션에 jwt 토큰 생성에 필요한 유저 정보를 임시로 저장한다.
 * 리다이렉트 주소는 "/login/success"로 브라우저의 로컬 스토리지에 jwt 토큰을 저장하는 과정을 처리하는 javascript 파일을 실행하기 위한 주소이다.
 * javascript 구현은 pront end 에서 구현하였다.
 *
 * 로그인 토큰 생성 및 발급은 AdminController.java에 위임하여 login() 함수에서 실행한다.
 */
@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("success handle");
        UserToken principal = (UserToken) authentication.getPrincipal();

        String userId = principal.getUsername();
        String password = principal.getPassword();
        HttpSession session = request.getSession(true);
        session.setAttribute("userId", userId);
        session.setAttribute("password", password);
        response.sendRedirect("/login/success");
    }
}
