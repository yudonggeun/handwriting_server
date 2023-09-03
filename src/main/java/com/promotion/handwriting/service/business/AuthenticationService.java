package com.promotion.handwriting.service.business;

import com.promotion.handwriting.entity.User;
import com.promotion.handwriting.security.UserToken;
import com.promotion.handwriting.repository.database.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * spring security 유저의 인증을 책임하는 클래스
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService extends DefaultOAuth2UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserService userService;

    /**
     * 일반 유저 로그인시 인증
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            return new UserToken(userRepository.findByUserId(username));
        } catch (NoSuchElementException ex){
            throw new UsernameNotFoundException("NoSuchElementException");
        }
    }

    /**
     * Oauth 로그인 시도시 Oauth로 넘겨받은 유저가 이미 가입한 상태인지를 판단한다.
     * - 가입 유저 : 가입 유저는 인증 처리한다.
     * - 미가입 유저 : 미가입 유저는 미인증 처리한다.
     * @param userRequest
     * @return UserToken 커스텀 객체
     * @throws OAuth2AuthenticationException 인증 실패
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> info = oAuth2User.getAttributes();

        String userId;
        String password;
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info(registrationId + " Oauth loadUser");
        log.info("info : " + info);

        if(registrationId.equals("google")){
            userId = (String) info.getOrDefault("email", "");
            password = registrationId + "-" + info.getOrDefault("sub", "");
        } else if(registrationId.equals("kakao")){
            userId = info.getOrDefault("id", "") + "";
            password = registrationId + "-" + ((Map<Object, Object>) info.getOrDefault("properties", List.of(""))).get("nickname");
            userService.join(userId, password);
        } else {
            throw new OAuth2AuthenticationException("not support registration");
        }

        try {
            log.info(userId + ", " + password);
            User user = userRepository.findByUserId(userId);
            UserToken userToken = new UserToken(user);

            if (!encoder.matches(password, user.getPassword()))
                throw new OAuth2AuthenticationException("password not match");
            return userToken;

        } catch (NoSuchElementException ex) {
            throw new OAuth2AuthenticationException("NoSuchElementException");
        }
    }
}
