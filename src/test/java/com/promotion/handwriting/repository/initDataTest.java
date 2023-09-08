package com.promotion.handwriting.repository;

import com.promotion.handwriting.TestClass;
import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.User;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.enums.UserType;
import com.promotion.handwriting.repository.database.AdRepository;
import com.promotion.handwriting.repository.database.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;

@DisplayName("초기 데이터 검증")
public class initDataTest extends TestClass {

    @Autowired
    AdRepository adRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;

    @DisplayName("메인페이지 정보는 반드시 하나 존재한다.")
    @Test
    void dataInitByDataSQL(){
        Ad mainPageData = adRepository.findByType(AdType.INTRO);
        assertThat(mainPageData).isNotNull();
    }

    @DisplayName("초기 관리자 계정이 존재한다")
    @Test
    void initAdminUser() {
        // given // when
        User admin = userRepository.findByUserId("admin");
        //then
        assertThat(admin).extracting("userId", "role")
                .containsExactly("admin",UserType.ADMIN);
        assertThat(encoder.matches("1234", admin.getPassword()));
    }
}
