package com.promotion.handwriting.api;

import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.service.business.DataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ExceptionHandleTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    DataService dataService;

    @DisplayName("예외 발생 시")
    @Test
    public void hookException() throws Exception {
        given(dataService.getContentDtos(AdType.CONTENT))
                .willThrow(RuntimeException.class);
        this.mockMvc.perform(get("/data/content").accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.status").value("fail")
                );
    }
}
