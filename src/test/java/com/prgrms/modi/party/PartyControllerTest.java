package com.prgrms.modi.party;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.MessageFormat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PartyControllerTest {

    @Autowired
    MockMvc mockMvc;

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

}
