package com.promotion.handwriting.api;

import com.promotion.handwriting.controller.DataController;
import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.image.UrlImageDto;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.service.business.DataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class DataApiTest extends RestDocs {
    @MockBean
    private DataService dataService;

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
        PageRequest pageable = PageRequest.of(0, 1);
        given(dataService.getContentDtos(any(), any()))
                .willReturn(new PageImpl<>(List.of(ContentDto.builder()
                        .id(id)
                        .title(title)
                        .description(description)
                        .images(List.of(
                                UrlImageDto.make("/image/origin/test.jpg", "/image/compress/test.jpg"),
                                UrlImageDto.make("/image/origin/test.jpg", "/image/compress/test.jpg")
                        ))
                        .build()), pageable, 2));

        this.mockMvc.perform(get("/data/content?page=0&size=1")
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print()
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.responseTime").exists(),
                jsonPath("$.status").value("success"),
                jsonPath("$.data").exists(),
                jsonPath("$.data.content[0].id").value(id),
                jsonPath("$.data.content[0].title").value(title),
                jsonPath("$.data.content[0].description").value(description),
                jsonPath("$.data.content[0].images[0].original").value("/image/origin/test.jpg"),
                jsonPath("$.data.content[0].images[0].compress").value("/image/compress/test.jpg")
        ).andDo(document("get-content",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                        field(
                                commonFieldDescriptors(),
                                pageResponseFieldDescriptors(),
                                List.of(
                                        fieldWithPath("data.content[].id").type(JsonFieldType.STRING)
                                                .description("컨텐츠 식별 id"),
                                        fieldWithPath("data.content[].title").type(JsonFieldType.STRING)
                                                .description("컨텐츠 제목"),
                                        fieldWithPath("data.content[].description").type(JsonFieldType.STRING)
                                                .description("컨텐츠 상세 정보"),
                                        fieldWithPath("data.content[].images").type(JsonFieldType.ARRAY)
                                                .description("컨텐츠 이미지 경로 정보"),
                                        fieldWithPath("data.content[].images[].original").type(JsonFieldType.STRING)
                                                .description("원본 이미지 경로"),
                                        fieldWithPath("data.content[].images[].compress").type(JsonFieldType.STRING)
                                                .description("압축 이미지 경로")
                                )
                        )
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

        given(dataService.getContentDtos(any(), any()))
                .willReturn(new PageImpl<>(List.of(dto), PageRequest.of(0, 1), 1));


        mockMvc.perform(get("/data/intro")).andDo(print())
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

    private List<FieldDescriptor> commonFieldDescriptors() {
        return List.of(
                fieldWithPath("responseTime").description("응답 시간"),
                fieldWithPath("status").type(JsonFieldType.STRING).description("응답 상태")
        );
    }

    private List<FieldDescriptor> pageResponseFieldDescriptors() {
        return List.of(
                fieldWithPath("data.totalElements").description("전체 컨텐츠 개수"),
                fieldWithPath("data.totalPages").description("전체 페이지 개수"),
                fieldWithPath("data.pageable.offset").description("페이지 offset"),
                fieldWithPath("data.pageable.pageNumber").description("페이지 넘버"),
                fieldWithPath("data.pageable.pageSize").ignored(),
                fieldWithPath("data.pageable.paged").description("페이징 여부"),
                fieldWithPath("data.pageable.unpaged").ignored(),
                fieldWithPath("data.pageable.sort.empty").ignored(),
                fieldWithPath("data.pageable.sort.sorted").ignored(),
                fieldWithPath("data.pageable.sort.unsorted").ignored(),
                fieldWithPath("data.last").description("마지막 페이지 여부").type(JsonFieldType.BOOLEAN),
                fieldWithPath("data.size").description("페이지 크기"),
                fieldWithPath("data.number").description("데이터 수"),
                fieldWithPath("data.first").description("첫 페이지 여부"),
                fieldWithPath("data.numberOfElements").ignored(),
                fieldWithPath("data.empty").description("빈 페이지 여부"),
                fieldWithPath("data.sort.empty").ignored(),
                fieldWithPath("data.sort.sorted").ignored(),
                fieldWithPath("data.sort.unsorted").ignored()
        );
    }

    private FieldDescriptor[] field(List<FieldDescriptor>... fields) {
        List<FieldDescriptor> all = new LinkedList<>();
        for (List<FieldDescriptor> field : fields) all.addAll(field);
        return all.toArray(new FieldDescriptor[0]);
    }
}
