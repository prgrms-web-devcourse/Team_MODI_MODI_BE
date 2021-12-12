package com.prgrms.modi.party.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class RuleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("모든 규칙 태그를 조회할 수 있다")
    public void getAllRule() throws Exception {
        // When
        mockMvc
            .perform(get("/api/rules"))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.rules[0].ruleId").value(1),
                jsonPath("$.rules[0].ruleName").value("1인 1회선")
            )
            .andDo(print());
    }

}
