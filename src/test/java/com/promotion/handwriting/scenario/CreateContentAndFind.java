package com.promotion.handwriting.scenario;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.promotion.handwriting.TestClass;
import com.promotion.handwriting.dto.request.CreateContentRequest;
import com.promotion.handwriting.dto.request.SearchContentsRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static com.promotion.handwriting.enums.AdType.CONTENT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("컨텐츠를 생성하고 조회하면")
@AutoConfigureMockMvc
public class CreateContentAndFind extends TestClass {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @DisplayName("컨텐츠의 내용이 저장되어야 한다.")
    @Test
    void mustExistContentExactly() throws Exception {
        // given
        var createContent = new CreateContentRequest();
        createContent.setTitle("new content");
        createContent.setDescription("new detail");
        var dto = new MockMultipartFile("dto", "dto", "application/json", mapper.writeValueAsString(createContent).getBytes());
        mockMvc.perform(multipart(HttpMethod.PUT, "/data/content").file(dto))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value("success")
                );
        // when then
        var searchRequest = new SearchContentsRequest();
        searchRequest.setType(CONTENT);
        mockMvc.perform(get("/data/content")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(searchRequest)))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.status").value("success"),
                        jsonPath("$.data.content[?(@.title=='new content')]").exists()
                );
    }

}
