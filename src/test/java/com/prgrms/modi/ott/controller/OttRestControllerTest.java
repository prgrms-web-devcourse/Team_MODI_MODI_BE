package com.prgrms.modi.ott.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OttRestControllerTest {

    private final String BASE_URL = "/api/otts";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Test
    @DisplayName("OTT 전체 조회 테스트")
    void getAllOtts() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                jsonPath("$.ottServices[0].ottName").value("넷플릭스"),
                jsonPath("$.ottServices[1].ottName").value("디즈니+"),
                jsonPath("$.ottServices[2].ottName").value("웨이브"),
                jsonPath("$.ottServices[3].ottName").value("와챠"),
                jsonPath("$.ottServices[4].ottName").value("티빙")
            );
    }

}
