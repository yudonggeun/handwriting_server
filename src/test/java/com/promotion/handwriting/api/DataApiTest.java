package com.promotion.handwriting.api;

import com.promotion.handwriting.controller.DataController;
import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.MainPageDto;
import com.promotion.handwriting.dto.ImageUrlDto;
import com.promotion.handwriting.dto.request.ChangeContentRequest;
import com.promotion.handwriting.dto.request.CreateContentRequest;
import com.promotion.handwriting.dto.request.DeleteContentRequest;
import com.promotion.handwriting.dto.request.DeleteImageRequest;
import com.promotion.handwriting.service.business.DataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class DataApiTest extends RestDocs {
    @MockBean
    private DataService dataService;

    @DisplayName("홍보 목록 조회 API")
    @Test
    public void getPromotionInfo() throws Exception {
        var id = "1";
        var title = "content title";
        var description = "content detail";
        PageRequest pageable = PageRequest.of(0, 1);
        given(dataService.getContentDtos(any(), any()))
                .willReturn(new PageImpl<>(List.of(contentDto(id, title, description, List.of(
                        new ImageUrlDto(1, "/image/origin/test.jpg", "/image/compress/test.jpg"),
                        new ImageUrlDto(2, "/image/origin/test.jpg", "/image/compress/test.jpg")
                ))), pageable, 2));

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
                        fields(
                                commonFieldDescriptors(),
                                pageResponseFieldDescriptors(),
                                List.of(
                                        fieldWithPath("data.content[].id").type(STRING)
                                                .description("컨텐츠 식별 id"),
                                        fieldWithPath("data.content[].title").type(STRING)
                                                .description("컨텐츠 제목"),
                                        fieldWithPath("data.content[].description").type(STRING)
                                                .description("컨텐츠 상세 정보"),
                                        fieldWithPath("data.content[].images").type(ARRAY)
                                                .description("컨텐츠 이미지 경로 정보"),
                                        fieldWithPath("data.content[].images[].original").type(STRING)
                                                .description("원본 이미지 경로"),
                                        fieldWithPath("data.content[].images[].compress").type(STRING)
                                                .description("압축 이미지 경로"),
                                        fieldWithPath("data.content[].images[].id").type(NUMBER)
                                                .description("이미지 id")
                                )
                        )
                )
        ));
    }


    @Deprecated
    @DisplayName("표지 정보 조회 API : 삭제 예정 url")
    @Test
    public void getIntro() throws Exception {

        var image = "/intro/image.jpg";
        given(dataService.mainPageData())
                .willReturn(new MainPageDto("title", image, "detail"));


        mockMvc.perform(get("/data/intro")).andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.responseTime").exists(),
                        jsonPath("$.status").value("success"),
                        jsonPath("$.data.title").value("title"),
                        jsonPath("$.data.imageUrl").value(image),
                        jsonPath("$.data.description").value("detail")
                )
                .andDo(
                        document("get-intro",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("responseTime")
                                                .description("응답 시간"),
                                        fieldWithPath("status").type(STRING)
                                                .description("응답 상태"),
                                        fieldWithPath("data").type(OBJECT)
                                                .description("컨텐츠 리스트"),
                                        fieldWithPath("data.imageUrl").type(STRING)
                                                .description("인트로 이미지 uri"),
                                        fieldWithPath("data.description").type(STRING)
                                                .description("페이지 소개글"),
                                        fieldWithPath("data.title").type(STRING)
                                                .description("페이지 제목")
                                )
                        )
                );
    }

    @DisplayName("특정한 컨텐츠의 이미지 정보를 조회 API")
    @Test
    public void getImageList() throws Exception {
        //given
        given(dataService.getImageUrls(any(), any()))
                .willReturn(new PageImpl<>(
                        List.of(new ImageUrlDto(1, "/orginal.jpg", "/compress.jpg")),
                        PageRequest.of(0, 1),
                        1
                ));

        //when //then
        mockMvc.perform(get("/data/content/image?content_id=1&page=0&size=5"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value("success"),
                        jsonPath("$.responseTime").exists(),
                        jsonPath("$.data.content[0].original").value("/orginal.jpg"),
                        jsonPath("$.data.content[0].compress").value("/compress.jpg")
                )
                .andDo(
                        document("get image list",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fields(
                                                commonFieldDescriptors(),
                                                pageResponseFieldDescriptors(),
                                                List.of(
                                                        fieldWithPath("data.content[].id").description("이미지 id"),
                                                        fieldWithPath("data.content[].original").description("원본 이미지 url"),
                                                        fieldWithPath("data.content[].compress").description("압축 이미지 url")
                                                )
                                        )
                                )

                        )
                );
    }

    @DisplayName("메인 페이지 갱신 API")
    @Test
    public void updateIntro() throws Exception {
        // given
        var dto = new MockMultipartFile("dto", "dto", "application/json",
                mapper.writeValueAsString(new MainPageDto("title", "/image.jpg", "detail")).getBytes());
        MockMultipartFile image = new MockMultipartFile("image", "image.png", "image/png", "<<png data>>".getBytes());
        // when //then
        mockMvc.perform(multipart("/data/intro")
                        .file(dto)
                        .file(image)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value("success"),
                        jsonPath("$.responseTime").exists()
                )
                .andDo(
                        document("post-intro",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fields(
                                                commonFieldDescriptors(),
                                                List.of(
                                                        fieldWithPath("data").ignored()
                                                )
                                        )
                                )
                        )
                );
    }

    @DisplayName("컨텐츠 정보 수정 API")
    @Test
    public void updateContent() throws Exception {
        // given
        var request = new ChangeContentRequest();
        request.setId(1);
        request.setTitle("change title");
        request.setDescription("change description");
        String requestBody = mapper.writeValueAsString(request);

        // when // then
        mockMvc.perform(post("/data/content")
                        .content(requestBody).contentType("application/json"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value("success"),
                        jsonPath("$.responseTime").exists()
                ).andDo(
                        document("post-content",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("id").description("컨텐츠 id").type(NUMBER),
                                        fieldWithPath("description").description("컨텐츠 부연 설명").type(STRING),
                                        fieldWithPath("title").description("컨텐츠 제목").type(STRING)
                                ),
                                responseFields(
                                        commonFieldDescriptors()
                                )
                        )
                );
    }

    @DisplayName("컨텐츠 삭제 API")
    @Test
    public void deleteContent() throws Exception {
        //given
        var request = new DeleteContentRequest();
        request.setContentId(1l);
        var requestString = mapper.writeValueAsString(request);
        // when //then
        mockMvc.perform(delete("/data/content")
                        .content(requestString)
                        .contentType("application/json"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value("success"),
                        jsonPath("$.responseTime").exists()
                ).andDo(
                        document("delete-content",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fields(
                                                commonFieldDescriptors()
                                        )
                                )
                        )
                );
    }

    @DisplayName("컨텐츠 추가 API")
    @Test
    public void createContent() throws Exception {
        // given
        var request = new CreateContentRequest();
        request.setTitle("my title");
        request.setDescription("my description");
        var dto = new MockMultipartFile("dto", "dto", "application/json",
                mapper.writeValueAsString(request).getBytes());
        var image = new MockMultipartFile("image", "image.png", "image/png", "<<png data>>".getBytes());
        // when //then
        mockMvc.perform(multipart(HttpMethod.PUT, "/data/content")
                        .file(dto)
                        .file(image)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value("success"),
                        jsonPath("$.responseTime").exists()
                )
                .andDo(
                        document("post-intro",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(commonFieldDescriptors())
                        )
                );
    }

    @DisplayName("컨텐츠 이미지 추가 API")
    @Test
    public void addDetailImages() throws Exception {
        //given
        var image = new MockMultipartFile("image", "image.png", "image/png", "<<png data>>".getBytes());
        //when //then
        mockMvc.perform(multipart(HttpMethod.PUT, "/data/detail/1")
                        .file(image)
                        .contentType("application/json")
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value("success"),
                        jsonPath("$.responseTime").exists()
                ).andDo(
                        document("put-content",
                                responseFields(commonFieldDescriptors())
                        )
                );
    }

    @DisplayName("이미지 삭제 API")
    @Test
    public void deleteDetailImages() throws Exception {
        //given
        var request = new DeleteImageRequest();
        request.setContentId(1l);
        request.setImageIds(List.of(1l, 2l));
        //when //then
        mockMvc.perform(delete("/data/detail")
                        .content(mapper.writeValueAsString(request))
                        .contentType("application/json"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value("success"),
                        jsonPath("$.responseTime").exists()
                ).andDo(
                        document("delete-image",
                                responseFields(commonFieldDescriptors())
                        )
                );
    }

    private List<FieldDescriptor> commonFieldDescriptors() {
        return List.of(
                fieldWithPath("responseTime").description("응답 시간"),
                fieldWithPath("status").type(STRING).description("응답 상태"),
                fieldWithPath("data").ignored()
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
                fieldWithPath("data.last").description("마지막 페이지 여부").type(BOOLEAN),
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

    private FieldDescriptor[] fields(List<FieldDescriptor>... fields) {
        List<FieldDescriptor> all = new LinkedList<>();
        for (List<FieldDescriptor> field : fields) all.addAll(field);
        return all.toArray(new FieldDescriptor[0]);
    }

    private ContentDto contentDto(String id, String title, String description, List<ImageUrlDto> images) {
        ContentDto dto = new ContentDto();
        dto.setId(id);
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setImages(images);
        return dto;
    }
}
