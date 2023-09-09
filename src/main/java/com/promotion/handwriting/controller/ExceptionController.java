package com.promotion.handwriting.controller;


import com.promotion.handwriting.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import static com.promotion.handwriting.dto.response.ApiResponse.fail;

@Slf4j
@RestControllerAdvice
public class ExceptionController {


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({IllegalStateException.class, ConstraintViolationException.class})
    public ApiResponse serverErrorHandle5XX(Exception e) {
        String message = "내부 서버에 이상이 있습니다. 관리자에게 문의해주세요.";
        log.error(e.getMessage());
        e.printStackTrace();
        return fail(message);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ApiResponse exceptionHandle(Exception e) {
        e.printStackTrace();
        return fail(null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, NoResultException.class, NonUniqueResultException.class})
    public ApiResponse userExceptionHandle4XX(Exception e) {
        return fail(e.getMessage());
    }

}
