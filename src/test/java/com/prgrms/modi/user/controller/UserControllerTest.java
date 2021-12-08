package com.prgrms.modi.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.modi.user.controller.UserController;
import com.prgrms.modi.user.security.WithMockJwtAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {

    private final String BASE_URL = "/api/users";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockJwtAuthentication
    @DisplayName("유저 정보 조회 성공 테스트")
    void findUserSuccessTest() throws Exception {
        int userId = 1;
        mockMvc
            .perform(
                get(BASE_URL + "/me")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpectAll(
                status().isOk(),
                handler().handlerType(UserController.class),
                handler().methodName("getUserDetail"),
                jsonPath("$.userId").value(userId),
                jsonPath("$.username").value("테스트 유저1"),
                jsonPath("$.points").value(0)
            )
            .andDo(print());
    }

}
