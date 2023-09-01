package com.promotion.handwriting.controller;


import com.promotion.handwriting.dto.response.ApiResponse;
import com.promotion.handwriting.enums.ApiResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({Exception.class})
    public ApiResponse exceptionHandle(Exception e){
        e.printStackTrace();
        return ApiResponse.fail(ApiResponseStatus.FAIL, null);
    }

}
