package com.promotion.handwriting.service.business;

import com.promotion.handwriting.entity.User;
import com.promotion.handwriting.enums.UserType;
import com.promotion.handwriting.repository.database.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public boolean login(String id, String pw) {
        User user = userRepository.findByUserId(id).orElseThrow();
        log.info("password : " + pw + " match result : " + encoder.matches(pw, user.getPassword()));
        return encoder.matches(pw, user.getPassword());
    }

    @Override
    public boolean join(String id, String pw) {
        long userCount = userRepository.count();
        if (userCount != 0) return false;
        userRepository.save(User.builder()
                .userId(id)
                .password(encoder.encode(pw))
                .type(UserType.OWNER).build());
        return true;
    }
}
