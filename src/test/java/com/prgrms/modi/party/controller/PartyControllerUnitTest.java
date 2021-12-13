package com.prgrms.modi.party.controller;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.modi.party.dto.request.CreatePartyRequest;
import com.prgrms.modi.party.dto.request.RuleRequest;
import com.prgrms.modi.party.service.PartyService;
import com.prgrms.modi.user.security.WithMockJwtAuthentication;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class PartyControllerUnitTest {

    @MockBean
    PartyService partyService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    @DisplayName("파티 목록 조회")
    public class PartyListTest {

        @Test
        @DisplayName("파티 목록 조회 - OTT ID는 양수이다")
        public void partyListInvalidOtt() throws Exception {
            long positiveOttId = 1;
            long negativeOttId = -1;
            long zeroOttId = 0;

            mockMvc
                .perform(get(MessageFormat.format("/api/otts/{0}/parties?size=1", positiveOttId)))
                .andExpect(status().isOk())
                .andDo(print());

            mockMvc
                .perform(get(MessageFormat.format("/api/otts/{0}/parties?size=1", zeroOttId)))
                .andExpect(status().isBadRequest())
                .andDo(print());

            mockMvc
                .perform(get(MessageFormat.format("/api/otts/{0}/parties?size=1", negativeOttId)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        }

        @Test
        @DisplayName("파티 목록 조회 - 페이지 크기는 양수이다")
        public void partyListInvalidSize() throws Exception {
            int positiveSize = 1;
            int zeroSize = 0;
            int negativeSize = -1;

            mockMvc
                .perform(get(MessageFormat.format("/api/otts/1/parties?size={0}", positiveSize)))
                .andExpect(status().isOk());

            mockMvc
                .perform(get(MessageFormat.format("/api/otts/1/parties?size={0}", zeroSize)))
                .andExpect(status().isBadRequest());

            mockMvc
                .perform(get(MessageFormat.format("/api/otts/1/parties?size={0}", negativeSize)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("파티 목록 조회 - 이전 파티 ID는 양수이다")
        public void partyListInvalidLastPartyId() throws Exception {
            int positivePartyId = 1;
            int zeroPartyId = 0;
            int negativePartyId = -1;

            mockMvc
                .perform(get(MessageFormat.format("/api/otts/1/parties?size=1&lastPartyId={0}", positivePartyId)))
                .andExpect(status().isOk());

            mockMvc
                .perform(get(MessageFormat.format("/api/otts/1/parties?size=1&lastPartyId={0}", zeroPartyId)))
                .andExpect(status().isBadRequest());

            mockMvc
                .perform(get(MessageFormat.format("/api/otts/1/parties?size=1&lastPartyId={0}", negativePartyId)))
                .andExpect(status().isBadRequest());
        }

    }


    @Test
    @DisplayName("파티 상세 조회 - 파티 ID는 양수이다")
    public void partyDetailInvalidId() throws Exception {
        int positivePartyId = 1;
        int zeroPartyId = 0;
        int negativePartyId = -1;

        mockMvc
            .perform(get(MessageFormat.format("/api//parties/{0}", positivePartyId)))
            .andExpect(status().isOk());

        mockMvc
            .perform(get(MessageFormat.format("/api//parties/{0}", zeroPartyId)))
            .andExpect(status().isBadRequest());

        mockMvc
            .perform(get(MessageFormat.format("/api//parties/{0}", negativePartyId)))
            .andExpect(status().isBadRequest());
    }

    @Nested
    @DisplayName("파티 공유 계정 조회")
    public class PartySharedAccountTest {

        @Test
        @DisplayName("파티 공유 계정 조회 - 토큰 없이 접근할 수 없다")
        public void partySharedAccountInvalidToken() throws Exception {
            mockMvc
                .perform(get("/api/parties/1/sharedAccount"))
                .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockJwtAuthentication
        @DisplayName("파티 공유 계정 조회 - 파티 미참여 유저는 접근할 수 없다")
        public void partySharedAccountInvalidUser() throws Exception {
            when(partyService.isPartyMember(any(Long.class), any(Long.class))).thenReturn(false);

            mockMvc
                .perform(get("/api/parties/1/sharedAccount"))
                .andExpect(status().isForbidden());
        }

    }

    @Nested
    @DisplayName("파티 생성")
    public class CreatePartyTest {

        @Test
        @DisplayName("파티 생성 - 누락된 생성 요청 바디")
        public void createWithMissingBody() throws Exception {
            final int REQUIRED_REQ_BODY_FIELDS = 8;
            var req = new CreatePartyRequest.Builder().build();

            mockMvc
                .perform(
                    post("/api/parties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.*", hasSize(REQUIRED_REQ_BODY_FIELDS))
                );
        }

        @Test
        @DisplayName("파티 생성 - 잘못된 생성 요청 바디")
        public void createWithInvalidBody() throws Exception {
            final int INVALID_BODY_FIELDS = 5;
            var req = new CreatePartyRequest.Builder()
                .ottId(-1L)
                .partyMemberCapacity(-1)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .mustFilled(false)
                .rules(List.of(new RuleRequest(-1L, "잘못된 규칙")))
                .sharedId("")
                .sharedPassword("")
                .build();

            mockMvc
                .perform(
                    post("/api/parties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.*", hasSize(INVALID_BODY_FIELDS))
                )
                .andDo(print());
        }

        @Test
        @DisplayName("파티 생성 - 토큰 없이 생성할 수 없다")
        public void createWithoutToken() throws Exception {
            var req = new CreatePartyRequest.Builder()
                .ottId(1L)
                .partyMemberCapacity(1)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .mustFilled(false)
                .rules(List.of(new RuleRequest(1L, "")))
                .sharedId("testId")
                .sharedPassword("testPw")
                .build();

            mockMvc
                .perform(
                    post("/api/parties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                )
                .andExpect(status().isUnauthorized());
        }

    }

}
