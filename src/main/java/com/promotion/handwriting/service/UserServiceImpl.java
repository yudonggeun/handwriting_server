package com.promotion.handwriting.service;

import com.promotion.handwriting.entity.User;
import com.promotion.handwriting.enums.UserType;
import com.promotion.handwriting.repository.UserRepository;
import com.promotion.handwriting.security.UserToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public boolean login(String id, String pw) {
        try {
            User user = userRepository.findById(id).orElseThrow();
            log.info("password : " + pw + " match result : " + encoder.matches(pw, user.getPassword()));
            return encoder.matches(pw, user.getPassword());
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean join(String id, String pw) {
        long userCount = userRepository.count();
        if(userCount != 0){
            return false;
        }
        User newUser = new User(id, encoder.encode(pw), UserType.OWNER);
        userRepository.save(newUser);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username).orElseThrow();
        return new UserToken(user);
    }
}
