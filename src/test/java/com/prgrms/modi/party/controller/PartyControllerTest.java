package com.prgrms.modi.party.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.prgrms.modi.history.domain.CommissionHistory;
import com.prgrms.modi.history.domain.PointHistory;
import com.prgrms.modi.history.repository.CommissionHistoryRepository;
import com.prgrms.modi.history.repository.PointHistoryRepository;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.dto.request.CreatePartyRequest;
import com.prgrms.modi.party.dto.request.RuleRequest;
import com.prgrms.modi.party.dto.request.UpdateSharedAccountRequest;
import com.prgrms.modi.party.repository.PartyRepository;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.repository.MemberRepository;
import com.prgrms.modi.user.repository.UserRepository;
import com.prgrms.modi.user.security.WithMockJwtAuthentication;
import com.prgrms.modi.utils.Encryptor;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class PartyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CommissionHistoryRepository commissionHistoryRepository;

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Autowired
    Encryptor encryptor;

    @Test
    @DisplayName("?????? ????????? ????????? ??? ??????")
    public void getParty() throws Exception {
        long partyId = 1L;

        mockMvc
            .perform(get("/api/parties/" + partyId))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.partyId").value(partyId),
                jsonPath("$.ottId").value(1L),
                jsonPath("$.members", hasSize(4)),
                jsonPath("$.rules", hasSize(3))
            )
            .andDo(print());
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("?????? ?????? ????????? ????????? ??? ??????")
    public void getSharedAccount() throws Exception {
        long partyId = 1L;

        mockMvc
            .perform(get("/api/parties/" + partyId + "/sharedAccount"))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.sharedId").value("modi112@gmail.com"),
                jsonPath("$.sharedPassword").value("modi")
            )
            .andDo(print());
    }


    @Test
    @DisplayName("OTT ?????? ?????? ??? ???????????? ????????? ??? ??????")
    public void getPartyList() throws Exception {
        int ottId = 1;
        int size = 5;

        mockMvc
            .perform(
                get(MessageFormat.format("/api/otts/{0}/parties?size={1}", ottId, size)))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.ottId").value(ottId),
                jsonPath("$.partyList[0].partyId").value(6),
                jsonPath("$.partyList", hasSize(5))
            )
            .andDo(print());
    }

    @Test
    @DisplayName("OTT ?????? ????????? ?????? ?????? ???????????? ????????? ??? ??????")
    public void getPartyListPage() throws Exception {
        int ottId = 1;
        int size = 5;
        long lastPartyId = 5;

        mockMvc
            .perform(
                get(MessageFormat.format("/api/otts/{0}/parties?size={1}&lastPartyId={2}", ottId, size, lastPartyId))
            )
            .andExpectAll(
                status().isOk(),
                jsonPath("$.ottId").value(ottId),
                jsonPath("$.totalSize").value(6),
                jsonPath("$.partyList[0].partyId").value(4)
            );
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("????????? ????????? ??? ??????")
    @Transactional(readOnly = true)
    public void createParty() throws Exception {
        // Given
        Long ottId = 1L;
        RuleRequest ruleRequest1 = new RuleRequest(1L, "1??? 1??????");
        RuleRequest ruleRequest2 = new RuleRequest(2L, "?????? ??????");
        CreatePartyRequest createPartyRequest = new CreatePartyRequest.Builder()
            .ottId(ottId)
            .ottName("????????????")
            .grade("????????????")
            .partyMemberCapacity(4)
            .startDate(LocalDate.now().plusDays(1))
            .endDate(LocalDate.now().plusDays(1).plusMonths(6))
            .mustFilled(true)
            .rules(List.of(ruleRequest1, ruleRequest2))
            .sharedId("modi@gmail.com")
            .sharedPassword("modimodi123")
            .build();

        // When
        var result = mockMvc
            .perform(
                post("/api/parties")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createPartyRequest))
            ).andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        // Then
        Long maybePartyId = JsonPath
            .parse(result.getResponse().getContentAsString())
            .read("$.partyId", Long.class);
        Party maybeParty = partyRepository.getById(maybePartyId);

        assertThat(maybeParty.getOtt().getId(), equalTo(ottId));
        assertThat(maybeParty.getSharedId(), equalTo("modi@gmail.com"));
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("????????? ????????? ??? ??????.")
    @Transactional(readOnly = true)
    public void joinParty() throws Exception {
        int userPoint = 150_000;
        Long userId = 1L;
        Long partyId = 6L;
        User user = userRepository.findById(userId).get();
        user.addPoints(userPoint);

        mockMvc.perform(post("/api/parties/{partyId}/join", partyId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.partyId").value(partyId)
            )
            .andDo(print());

        Party party = partyRepository.findById(partyId).get();
        int commission = (int) (party.getTotalPrice() * 0.05);
        List<PointHistory> pointHistoryList = pointHistoryRepository.findAllByUserId(userId);
        List<CommissionHistory> commissionHistoryList = commissionHistoryRepository.findAllByUserId(userId);

        assertThat(user.getPoints(), equalTo(userPoint - (party.getTotalPrice() + commission)));
        assertThat(party.getCurrentMember(), equalTo(3));
        assertThat(party.getMonthlyReimbursement(), equalTo(2500));
        assertThat(party.getRemainingReimbursement(), equalTo(party.getTotalPrice()));
        assertThat(pointHistoryList.size(), equalTo(1));
        assertThat(commissionHistoryList.size(), equalTo(1));
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("???????????? ????????? ?????? ????????? ????????? ??? ??????.")
    @Transactional(readOnly = true)
    public void notEnoughPointTest() throws Exception {
        Long partyId = 6L;

        mockMvc.perform(post("/api/parties/{partyId}/join", partyId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print());

    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("????????? ????????? ???????????? ????????? ??? ??????.")
    @Transactional(readOnly = true)
    public void notEnoughPartyCapacityTest() throws Exception {
        Long partyId = 1L;

        mockMvc.perform(post("/api/parties/{partyId}/join", partyId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print());
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("????????? ?????? ????????? ??? ??? ??????.")
    @Transactional
    public void alreadyJoinedExceptionTest() throws Exception {
        Long partyId = 5L;
        Long userId = 1L;
        User user = userRepository.findById(userId).get();
        int userPoint = 100_000;
        user.addPoints(userPoint);

        mockMvc.perform(post("/api/parties/{partyId}/join", partyId)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isBadRequest(),
                jsonPath("$.errorMessage").value("?????? ????????? ???????????????")
            )
            .andDo(print());
    }

    @Test
    @DisplayName("?????? ?????? ????????? ????????? ??? ??????")
    public void getAllRule() throws Exception {
        // When
        mockMvc
            .perform(get("/api/rules"))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.rules[0].ruleId").value(1),
                jsonPath("$.rules[0].ruleName").value("1??? 1??????")
            )
            .andDo(print());
    }

    @Test
    @DisplayName("????????? ????????? ??? ??????")
    @WithMockJwtAuthentication
    @Transactional
    public void deleteParty() throws Exception {
        long partyId = 8L;
        assertTrue(partyRepository.findById(partyId).isPresent());

        mockMvc
            .perform(delete("/api/parties/" + partyId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isNoContent()
            );
        assertEquals(partyRepository.findById(partyId), Optional.empty());
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("????????? ?????? ????????? ????????? ??? ??????.")
    public void updateSharedAccount() throws Exception {
        long partyId = 2L;
        long ottId = 1L;
        String updatedSharedPassword = "newmodi";
        UpdateSharedAccountRequest request = new UpdateSharedAccountRequest(updatedSharedPassword);

        mockMvc
            .perform(patch("/api/parties/" + partyId + "/sharedAccount/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            )
            .andExpectAll(
                status().isOk()
            );
    }

}
