package com.promotion.handwriting.service.business;

import java.io.IOException;

public interface UserService {
    void login(String id, String pw);
    void join(String id, String pw);
}
