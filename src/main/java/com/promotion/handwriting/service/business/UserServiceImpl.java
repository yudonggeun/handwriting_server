package com.promotion.handwriting.service.business;

import com.promotion.handwriting.dto.response.LoginSuccessResponse;
import com.promotion.handwriting.entity.User;
import com.promotion.handwriting.enums.UserType;
import com.promotion.handwriting.repository.database.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public LoginSuccessResponse login(String id, String pw) {
        try {
            User user = userRepository.findByUserId(id);
            if(!encoder.matches(pw, user.getPassword())) throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            var result = new LoginSuccessResponse();
            result.setRole(user.getRole());
            return result;
        } catch (NoResultException | NonUniqueResultException e) {
            throw new IllegalArgumentException("해당 아이디를 가진 유저가 없습니다.");
        }
    }

    @Override
    public void join(String id, String pw) {
        try{
            userRepository.save(User.builder()
                    .userId(id)
                    .password(encoder.encode(pw))
                    .type(UserType.ADMIN).build());
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("회원가입 실패");
        }
    }
}
