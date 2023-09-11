package com.promotion.handwriting.api;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ActuatorTest extends RestDocs{

    @Test
    void health() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpectAll(
                        status().isOk()
                );
    }
}
