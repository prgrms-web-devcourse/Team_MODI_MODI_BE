package com.prgrms.modi.user.controller;

import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.user.security.WithMockJwtAuthentication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.MessageFormat;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest {

    private final String BASE_URL = "/api/users";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockJwtAuthentication
    @DisplayName("유저의 정보를 조회할 수 있다")
    void getUserSuccessTest() throws Exception {
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

    @Test
    @WithMockJwtAuthentication
    @DisplayName("유저의 포인트를 조회할 수 있다")
    void getUserPointsSuccessTest() throws Exception {
        mockMvc
            .perform(
                get(BASE_URL + "/me/points")
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpectAll(
                status().isOk(),
                handler().handlerType(UserController.class),
                handler().methodName("getUserPoints"),
                jsonPath("$.points").value(0)
            )
            .andDo(print());
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("유저가 참여한 특정 파티를 조회할 수 있다")
    public void getUserParty() throws Exception {
        long partyId = 1L;

        mockMvc
            .perform(get("/api/users/me/parties/" + partyId))
            .andExpectAll(
                status().isOk(),
                handler().handlerType(UserController.class),
                handler().methodName("getUserPartyDetail"),
                jsonPath("$.partyId").value(partyId),
                jsonPath("$.ottId").value(1L),
                jsonPath("$.members", hasSize(4)),
                jsonPath("$.rules", hasSize(3))
            )
            .andDo(print());
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("유저 RECRUITING 파티 조회 테스트")
    void getUserRecruitingPartiesTest() throws Exception {
        PartyStatus partyStatus = PartyStatus.RECRUITING;
        int size = 5;
        long lastPartyId = 5;
        mockMvc
            .perform(
                get(MessageFormat
                    .format("/api/users/me/parties?status={0}&size={1}&lastPartyId={2}", partyStatus, size,
                        lastPartyId
                    )
                )
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpectAll(
                status().isOk(),
                handler().handlerType(UserController.class),
                handler().methodName("getUserPartyList"),
                jsonPath("$.parties[0].partyId").value(lastPartyId - 1),
                jsonPath("$.parties[0].startDate").value("2021-12-26")
            )
            .andDo(print());
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("유저 ONGOING 파티 조회 테스트")
    void getUserOnGoingPartiesTest() throws Exception {
        PartyStatus partyStatus = PartyStatus.ONGOING;
        int size = 5;
        long lastPartyId = 5;
        mockMvc
            .perform(
                get(MessageFormat
                    .format("/api/users/me/parties?status={0}&size={1}&lastPartyId={2}", partyStatus, size,
                        lastPartyId
                    )
                )
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpectAll(
                status().isOk(),
                handler().handlerType(UserController.class),
                handler().methodName("getUserPartyList"),
                jsonPath("$.parties[0].partyId").value(lastPartyId - 2),
                jsonPath("$.parties[0].startDate").value("2021-11-02")
            )
            .andDo(print());
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("유저 FINISHED 파티 조회 테스트")
    void getUserFinishedPartiesTest() throws Exception {
        PartyStatus partyStatus = PartyStatus.FINISHED;
        int size = 5;
        mockMvc
            .perform(
                get(MessageFormat
                    .format("/api/users/me/parties?status={0}&size={1}", partyStatus, size)
                )
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpectAll(
                status().isOk(),
                handler().handlerType(UserController.class),
                handler().methodName("getUserPartyList"),
                jsonPath("$.parties").isEmpty()
            )
            .andDo(print());
    }

    @Test
    @DisplayName("닉네임 자동생성 API 테스트")
    public void getGeneratedUsername() throws Exception {
        mockMvc
            .perform(get("/api/users/generate-username"))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.generatedUsername").isNotEmpty()
            );
    }

}
