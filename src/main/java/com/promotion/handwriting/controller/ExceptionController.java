package com.promotion.handwriting.controller;


import com.promotion.handwriting.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import static com.promotion.handwriting.dto.response.ApiResponse.fail;
import static com.promotion.handwriting.enums.ApiResponseStatus.FAIL;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({IllegalStateException.class})
    public ApiResponse serverErrorHandle5XX(Exception e){
        String message = "내부 서버에 이상이 있습니다. 관리자에게 문의해주세요.";
        log.error(e.getMessage());
        return fail(FAIL, message);
    }
    @ExceptionHandler({Exception.class})
    public ApiResponse exceptionHandle(Exception e){
        e.printStackTrace();
        return fail(FAIL, null);
    }

    @ExceptionHandler({IllegalArgumentException.class, NoResultException.class, NonUniqueResultException.class})
    public ApiResponse userExceptionHandle(Exception e){
        return fail(FAIL, e.getMessage());
    }

}
