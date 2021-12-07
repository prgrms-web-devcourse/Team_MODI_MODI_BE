package com.prgrms.modi.ott.service;

import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.dto.OttListResponse;
import com.prgrms.modi.ott.dto.OttResponse;
import com.prgrms.modi.ott.repository.OttRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OttServiceTest {

    @InjectMocks
    private OttService ottService;

    @Mock
    private OttRepository ottRepository;

    public OTT getOttFixture(Long id) {
        OTT ott = Mockito.mock(OTT.class);
        given(ott.getId()).willReturn(id);
        given(ott.getName()).willReturn("testOttName");
        given(ott.getSubscriptionFee()).willReturn(16000);
        given(ott.getMonthlyFee()).willReturn(4000);
        given(ott.getMaxMemberCapacity()).willReturn(4);
        given(ott.getGrade()).willReturn("프리미엄");
        return ott;
    }

    public OTT getOttFixture() {
        return getOttFixture(1L);
    }

    @Test
    @DisplayName("OTT 전체 조회 테스트")
    @Order(1)
    void getAllTest() {
        List<OTT> otts = Arrays.asList(getOttFixture(1L), getOttFixture(2L));
        given(ottRepository.findAll()).willReturn(otts);

        OttListResponse response = ottService.getAll();
        then(response.getOttServices())
            .hasSize(2)
            .extracting("ottId")
            .contains(1L, 2L);
    }

    @Test
    @DisplayName("OTT 단건 조회 테스트")
    @Order(2)
    void getOttTest() {
        OTT ott = getOttFixture();
        given(ottRepository.findById(anyLong())).willReturn(Optional.of(ott));

        OttResponse ottResponse = ottService.getOtt(1L);
        then(ottResponse)
            .hasFieldOrPropertyWithValue("ottId", ottResponse.getOttId())
            .hasFieldOrPropertyWithValue("ottName", ottResponse.getOttName())
            .hasFieldOrPropertyWithValue("subscriptionFee", ottResponse.getSubscriptionFee())
            .hasFieldOrPropertyWithValue("monthlyFee", ottResponse.getMonthlyFee())
            .hasFieldOrPropertyWithValue("maxMemberCapacity", ottResponse.getMaxMemberCapacity())
            .hasFieldOrPropertyWithValue("grade", ottResponse.getGrade());
    }

}
