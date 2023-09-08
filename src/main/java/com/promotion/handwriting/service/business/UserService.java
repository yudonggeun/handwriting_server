package com.promotion.handwriting.service.business;

import com.promotion.handwriting.dto.response.LoginSuccessResponse;

public interface UserService {
    LoginSuccessResponse login(String id, String pw);
    void join(String id, String pw);
}
