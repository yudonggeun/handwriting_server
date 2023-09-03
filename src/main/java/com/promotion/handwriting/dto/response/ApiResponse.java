package com.promotion.handwriting.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiResponse {
    private LocalDateTime responseTime;
    private String status;
    private Object data;

    private ApiResponse(String status, Object data) {
        this.responseTime = LocalDateTime.now();
        this.status = status;
        this.data = data;
    }

    static public ApiResponse success(Object data) {
        return new ApiResponse("success", data);
    }

    static public ApiResponse fail(Object data) {
        return new ApiResponse("fail", data);
    }
}
