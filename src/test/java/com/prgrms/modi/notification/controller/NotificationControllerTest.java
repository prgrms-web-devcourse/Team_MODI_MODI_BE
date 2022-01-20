package com.prgrms.modi.notification.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    @DisplayName("sse 연결 테스트")
    void sseConnectionTest() throws Exception {
        Long userId = 1L;
        var result = mockMvc
            .perform(
                get("/api/notifications/subscribe/{userId}", userId)
                    .accept(MediaType.TEXT_EVENT_STREAM_VALUE));

        result.andExpectAll(
            status().isOk(),
            content().contentType("text/event-stream;charset=UTF-8")
        )
            .andDo(print());
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("전체 알림 조회 테스트")
    void getAllNotifications() throws Exception {
        // When
        mockMvc
            .perform(get("/api/notifications")
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpectAll(
                status().isOk(),
                handler().handlerType(NotificationController.class),
                handler().methodName("getNotifications"),
                jsonPath("$.notificationResponseList[0].id").value(4),
                jsonPath("$.notificationResponseList[0].content").value("공유계정 정보가 변경되었습니다."),
                jsonPath("$.notificationResponseList[0].readCheck").value(false),
                jsonPath("$.notificationResponseList[0].createdAt").exists(),
                jsonPath("$.notificationResponseList[0].partyId").value(9),
                jsonPath("$.unreadCount").value(3)
            )
            .andDo(print());
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("알림 읽음(삭제) 테스트")
    void readNotification() throws Exception {
        long id = 3L;
        // When
        mockMvc
            .perform(patch("/api/notifications/" + id)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpectAll(
                status().isNoContent(),
                handler().handlerType(NotificationController.class),
                handler().methodName("readNotification")
            )
            .andDo(print());
    }

}
