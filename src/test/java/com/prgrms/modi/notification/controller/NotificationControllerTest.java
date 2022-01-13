package com.prgrms.modi.notification.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.modi.user.security.WithMockJwtAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class NotificationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("sse 연결")
    @WithMockJwtAuthentication
    void sse() throws Exception {
        var result = mockMvc
            .perform(
                get("/api/notifications/subscribe")
                    .accept(MediaType.TEXT_EVENT_STREAM_VALUE));

        result.andExpectAll(
            status().isOk(),
            content().contentType("text/event-stream;charset=UTF-8")
        )
            .andDo(print());
    }

}
