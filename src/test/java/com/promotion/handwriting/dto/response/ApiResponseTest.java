package com.promotion.handwriting.dto.response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ApiResponseTest {

    @DisplayName("success 응답을 생성한다.")
    @Test
    void success() {
        // given when
        ApiResponse response = ApiResponse.success("data");
        // then
        Assertions.assertThat(response).extracting("status", "data")
                .containsExactly("success", "data");
    }

    @DisplayName("fail 응답을 생성한다.")
    @Test
    void fail() {
        // given when
        ApiResponse response = ApiResponse.fail("fail message");
        // then
        Assertions.assertThat(response).extracting("status", "data")
                .containsExactly("fail", "fail message");
    }
}