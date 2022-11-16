package com.promotion.handwriting.service;

import java.io.IOException;

public interface LoginService {
    boolean login(String id, String pw) throws IOException;
}
