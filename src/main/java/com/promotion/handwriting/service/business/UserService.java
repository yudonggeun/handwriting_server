package com.promotion.handwriting.service.business;

import java.io.IOException;

public interface UserService {
    boolean login(String id, String pw) throws IOException;

    boolean join(String id, String pw);
}
