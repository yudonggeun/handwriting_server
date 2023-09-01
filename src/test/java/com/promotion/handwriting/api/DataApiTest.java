package com.promotion.handwriting.api;

import com.promotion.handwriting.controller.DataController;
import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.image.UrlImageDto;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.service.business.DataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DataApiTest extends RestDocs {
    private DataService dataService = mock(DataService.class);

    @Override
    protected Object initController() {
        return new DataController(dataService);
    }

    @DisplayName("홍보 목록 조회 API")
    @Test
    public void getPromotionInfo() throws Exception {
        var id = "1";
        var title = "content title";
        var description = "content detail";
        given(dataService.getContentDtos(AdType.CONTENT))
                .willReturn(List.of(ContentDto.builder()
                        .id(id)
                        .title(title)
                        .description(description)
                        .images(List.of(
                                UrlImageDto.make("/image/origin/test.jpg", "/image/compress/test.jpg")
                        ))
                        .build()));

        this.mockMvc.perform(get("/data/content").accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.responseTime").exists(),
                        jsonPath("$.status").value("success"),
                        jsonPath("$.data").exists(),
                        jsonPath("$.data[0].id").value(id),
                        jsonPath("$.data[0].title").value(title),
                        jsonPath("$.data[0].description").value(description),
                        jsonPath("$.data[0].images").exists(),
                        jsonPath("$.data[0].images[*].original").value("/image/origin/test.jpg"),
                        jsonPath("$.data[0].images[*].compress").value("/image/compress/test.jpg")
                )
                .andDo(document("get-content",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("responseTime")
                                        .description("응답 시간"),
                                fieldWithPath("status").type(JsonFieldType.STRING)
                                        .description("응답 상태"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                        .description("컨텐츠 리스트"),
                                fieldWithPath("data[].id").type(JsonFieldType.STRING)
                                        .description("컨텐츠 식별 id"),
                                fieldWithPath("data[].title").type(JsonFieldType.STRING)
                                        .description("컨텐츠 제목"),
                                fieldWithPath("data[].description").type(JsonFieldType.STRING)
                                        .description("컨텐츠 상세 정보"),
                                fieldWithPath("data[].images").type(JsonFieldType.ARRAY)
                                        .description("컨텐츠 이미지 경로 정보"),
                                fieldWithPath("data[].images[].original").type(JsonFieldType.STRING)
                                        .description("원본 이미지 경로"),
                                fieldWithPath("data[].images[].compress").type(JsonFieldType.STRING)
                                        .description("압축 이미지 경로")
                        )
                ));
    }

    @Deprecated
    @DisplayName("표지 정보 조회 : 삭제 예정 url")
    @Test
    public void getIntro() throws Exception {

        var image = "/intro/image.jpg";
        var dto = ContentDto.builder()
                .images(List.of(new UrlImageDto(image, "")))
                .id("001")
                .title("title")
                .description("글1#글2#글3")
                .build();

        given(dataService.getContentDtos(AdType.INTRO))
                .willReturn(List.of(dto));


        mockMvc.perform(get("/data/intro"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.responseTime").exists(),
                        jsonPath("$.status").value("success"),
                        jsonPath("$.data").exists(),
                        jsonPath("$.data.image").value(image),
                        jsonPath("$.data.comments").exists()
                )
                .andDo(
                        document("get-intro",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("responseTime")
                                                .description("응답 시간"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("응답 상태"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT)
                                                .description("컨텐츠 리스트"),
                                        fieldWithPath("data.image").type(JsonFieldType.STRING)
                                                .description("인트로 이미지 uri"),
                                        fieldWithPath("data.comments").type(JsonFieldType.ARRAY)
                                                .description("페이지 소개글 리스트")
                                )
                        )
                );
    }
}
