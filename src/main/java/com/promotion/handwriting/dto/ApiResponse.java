package com.promotion.handwriting.dto;

import com.promotion.handwriting.enums.ApiResponseStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiResponse {
    LocalDateTime responseTime;
    String status;
    Object data;

    @Builder
    public ApiResponse(ApiResponseStatus status, Object data) {
        this.responseTime = LocalDateTime.now();
        this.status = status.name().toLowerCase();
        this.data = data;
    }

    static public ApiResponse success(Object data){
        return ApiResponse.builder()
                .status(ApiResponseStatus.SUCCESS)
                .data(data).build();
    }

    static public ApiResponse fail(ApiResponseStatus status, Object data){
        return ApiResponse.builder()
                .status(status)
                .data(data).build();
    }
}
