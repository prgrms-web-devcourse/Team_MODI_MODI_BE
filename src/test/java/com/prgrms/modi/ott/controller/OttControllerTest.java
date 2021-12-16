package com.prgrms.modi.ott.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.repository.OttRepository;
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
class OttControllerTest {

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
                jsonPath("$.ottServices[1].ottName").value("왓챠"),
                jsonPath("$.ottServices[2].ottName").value("웨이브"),
                jsonPath("$.ottServices[3].ottName").value("티빙"),
                jsonPath("$.ottServices[4].ottName").value("디즈니 플러스"),
                jsonPath("$.ottServices[0].ottNameEn").value("netflix"),
                jsonPath("$.ottServices[1].ottNameEn").value("watcha"),
                jsonPath("$.ottServices[2].ottNameEn").value("wavve"),
                jsonPath("$.ottServices[3].ottNameEn").value("tving"),
                jsonPath("$.ottServices[4].ottNameEn").value("disneyPlus")
            );
    }

    @Test
    @DisplayName("OTT 단건 조회 테스트 - 성공")
    @Transactional
    void getOttTest() throws Exception {
        OTT ott = new OTT.Builder()
            .name("넷플릭스")
            .englishName("netflix")
            .subscriptionFee(16000)
            .monthlyPrice(4000)
            .maxMemberCapacity(4)
            .grade("프리미엄")
            .build();
        ott = ottRepository.save(ott);

        mockMvc.perform(get(BASE_URL + "/{id}", ott.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                jsonPath("$.ottId").value(ott.getId()),
                jsonPath("$.ottName").value(ott.getName()),
                jsonPath("$.ottNameEn").value(ott.getEnglishName()),
                jsonPath("$.subscriptionFee").value(ott.getSubscriptionFee()),
                jsonPath("$.monthlyPrice").value(ott.getMonthlyPrice()),
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

    @Test
    @DisplayName("캐루셀 전체 조회 테스트")
    void getAllCarousels() throws Exception {
        mockMvc.perform(get(BASE_URL + "/waitings")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpectAll(
                status().isOk(),
                jsonPath("$.waitingOtts[0].ottName").value("넷플릭스"),
                jsonPath("$.waitingOtts[1].ottName").value("왓챠"),
                jsonPath("$.waitingOtts[2].ottName").value("웨이브"),
                jsonPath("$.waitingOtts[3].ottName").value("티빙"),
                jsonPath("$.waitingOtts[4].ottName").value("디즈니 플러스"),
                jsonPath("$.waitingOtts[0].waitingForMatch").value(6L),
                jsonPath("$.waitingOtts[1].waitingForMatch").value(0L),
                jsonPath("$.waitingOtts[2].waitingForMatch").value(0L),
                jsonPath("$.waitingOtts[3].waitingForMatch").value(0L),
                jsonPath("$.waitingOtts[4].waitingForMatch").value(0L)
            )
            .andDo(print());
    }

}
