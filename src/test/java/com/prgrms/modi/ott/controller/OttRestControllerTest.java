package com.prgrms.modi.ott.controller;

import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.repository.OttRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OttRestControllerTest {

    private final String BASE_URL = "/api/otts";

    private final Long NOT_EXIST_ID = Long.MAX_VALUE;

    @Autowired
    private OttRepository ottRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("OTT 전체 조회 테스트")
    void getAllTest() throws Exception {
        mockMvc.perform(get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                jsonPath("$.ottServices[0].ottName").value("넷플릭스"),
                jsonPath("$.ottServices[1].ottName").value("디즈니+"),
                jsonPath("$.ottServices[2].ottName").value("웨이브"),
                jsonPath("$.ottServices[3].ottName").value("와챠"),
                jsonPath("$.ottServices[4].ottName").value("티빙")
            );
    }

    @Test
    @DisplayName("OTT 단건 조회 테스트 - 성공")
    void getOttTest() throws Exception {
        OTT ott = new OTT("멜론", 12000, 3000, 4, "프리미엄");
        ott = ottRepository.save(ott);

        mockMvc.perform(get(BASE_URL + "/{id}", ott.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                jsonPath("$.ottId").value(ott.getId()),
                jsonPath("$.ottName").value(ott.getName()),
                jsonPath("$.subscriptionFee").value(ott.getSubscriptionFee()),
                jsonPath("$.monthlyFee").value(ott.getMonthlyFee()),
                jsonPath("$.maxMemberCapacity").value(ott.getMaxMemberCapacity()),
                jsonPath("$.grade").value(ott.getGrade())
            );
    }

    @Test
    @DisplayName("OTT 단건 조회 테스트 - 실패")
    void getOttTestToFail() throws Exception {
        mockMvc.perform(get(BASE_URL + "/{id}", NOT_EXIST_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpectAll(
                status().isNotFound(),
                jsonPath("$.errorMessage").value("요청하신 Ott를 찾지 못했습니다.")
            );
    }

}
