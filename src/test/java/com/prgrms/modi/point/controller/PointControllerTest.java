package com.prgrms.modi.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.modi.point.dto.PointAddRequest;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.security.WithMockJwtAuthentication;
import com.prgrms.modi.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PointControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("포인트를 충전 할 수 있다.")
    @WithMockJwtAuthentication
    @Transactional
    void addPoints() throws Exception {
        int points = 10_000;
        PointAddRequest pointAddRequest = new PointAddRequest(points);

        mockMvc.perform(
                post("/api/points/add")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pointAddRequest))
            )
            .andExpect(status().isOk())
            .andDo(print());

        User user = userService.findUser(1L);
        assertThat(user.getPoints(), equalTo(points));
    }

}
