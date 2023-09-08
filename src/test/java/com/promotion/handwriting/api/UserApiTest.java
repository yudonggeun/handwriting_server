package com.promotion.handwriting.api;

import com.promotion.handwriting.dto.request.LoginRequest;
import com.promotion.handwriting.dto.response.LoginSuccessResponse;
import com.promotion.handwriting.enums.UserType;
import com.promotion.handwriting.service.business.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유저 관련 API")
public class UserApiTest extends RestDocs {

    @MockBean
    UserService userService;

    @DisplayName("로그인 API")
    @Nested
    class Login {
        @DisplayName("성공시")
        @Test
        void success() throws Exception {
            // given // when
            var result = new LoginSuccessResponse();
            result.setRole(UserType.ADMIN);
            given(userService.login(any(), any())).willReturn(result);
            var request = new LoginRequest();
            request.setId("admin");
            request.setPw("1234");
            // then
            mockMvc.perform(post("/admin/login")
                            .content(mapper.writeValueAsString(request))
                            .contentType("application/json"))
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value("success"),
                            jsonPath("$.responseTime").exists(),
                            jsonPath("$.data.role").value(result.getRole().name())
                    ).andDo(
                            document("login-success",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    requestFields(
                                            fieldWithPath("id").description("아이디"),
                                            fieldWithPath("pw").description("비밀번호")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").description("응답 결과 상태"),
                                            fieldWithPath("responseTime").description("응답 시간"),
                                            fieldWithPath("data.role").description("유저 권한")
                                    )
                            )
                    );
        }

        @DisplayName("실패시")
        @Test
        void fail() throws Exception {
            // given // when
            willThrow(new IllegalArgumentException("로그인 실패")).given(userService).login(any(), any());
            var request = new LoginRequest();
            request.setId("admin");
            request.setPw("1234");
            // then
            mockMvc.perform(post("/admin/login")
                            .content(mapper.writeValueAsString(request))
                            .contentType("application/json"))
                    .andDo(print())
                    .andExpectAll(
                            status().isOk(),
                            jsonPath("$.status").value("fail"),
                            jsonPath("$.responseTime").exists(),
                            jsonPath("$.data").value("로그인 실패")
                    ).andDo(
                            document("login-fail",
                                    preprocessRequest(prettyPrint()),
                                    preprocessResponse(prettyPrint()),
                                    requestFields(
                                            fieldWithPath("id").description("아이디"),
                                            fieldWithPath("pw").description("비밀번호")
                                    ),
                                    responseFields(
                                            fieldWithPath("status").description("응답 결과 상태"),
                                            fieldWithPath("responseTime").description("응답 시간"),
                                            fieldWithPath("data").description("응답 결과 메시지")
                                    )
                            )
                    );
        }
    }
}
