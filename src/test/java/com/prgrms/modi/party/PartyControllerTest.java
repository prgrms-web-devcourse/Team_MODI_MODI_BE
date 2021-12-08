package com.prgrms.modi.party;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.dto.request.CreatePartyRequest;
import com.prgrms.modi.party.dto.request.RuleRequest;
import com.prgrms.modi.party.repository.PartyRepository;
import com.prgrms.modi.user.security.WithMockJwtAuthentication;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
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

    @Test
    @DisplayName("OTT 파티 목록 첫 페이지를 조회할 수 있다")
    public void getPartyList() throws Exception {
        int ottId = 1;
        int size = 5;

        mockMvc
            .perform(
                get(MessageFormat.format("/api/otts/{0}/parties?size={1}", ottId, size)))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.ottId").value(ottId),
                jsonPath("$.partyList[0].partyId").value(5)
            )
            .andDo(print());
    }

    @Test
    @DisplayName("OTT 파티 목록을 커서 기반 페이지로 조회할 수 있다")
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
                jsonPath("$.partyList[0].partyId").value(4)
            );
    }

    @Test
    @DisplayName("존재하지 않는 OTT의 파티 목록을 조회할 수 없다")
    public void getPartyListInvalidOtt() throws Exception {
        long ottId = -1;
        int size = 5;

        mockMvc
            .perform(get(MessageFormat.format("/api/otts/{0}/parties?size={1}", ottId, size)))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    @DisplayName("파티 목록 조회 페이지 크기는 양수이다")
    public void pageSizePositive() throws Exception {
        int ottId = 1;
        int positiveSize = 1;
        mockMvc
            .perform(get(MessageFormat.format("/api/otts/{0}/parties?size={1}", ottId, positiveSize)))
            .andExpect(status().isOk());

        int zeroSize = 0;
        mockMvc
            .perform(get(MessageFormat.format("/api/otts/{0}/parties?size={1}", ottId, zeroSize)))
            .andExpect(status().isBadRequest());

        int negativeSize = -1;
        mockMvc
            .perform(get(MessageFormat.format("/api/otts/{0}/parties?size={1}", ottId, negativeSize)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockJwtAuthentication
    @DisplayName("파티를 생성할 수 있다")
    @Transactional(readOnly = true)
    public void createParty() throws Exception {
        // Given
        Long ottId = 1L;
        RuleRequest ruleRequest1 = new RuleRequest(1L, "1인 1회선");
        RuleRequest ruleRequest2 = new RuleRequest(2L, "양도 금지");
        CreatePartyRequest createPartyRequest = new CreatePartyRequest.Builder()
            .ottId(ottId)
            .ottName("넷플릭스")
            .grade("프리미엄")
            .partyMemberCapacity(4)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusMonths(6))
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

}