package com.prgrms.modi.ott.service;

import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.dto.OttListResponse;
import com.prgrms.modi.ott.repository.OttRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OttServiceTest {

    @InjectMocks
    private OttService ottService;

    @Mock
    private OttRepository ottRepository;

    public static OTT getOttFixture(Long id) {
        OTT ott = Mockito.mock(OTT.class);
        given(ott.getId()).willReturn(id);
        given(ott.getName()).willReturn("testOttName");
        return ott;
    }

    @Test
    @DisplayName("OTT 전체 조회 테스트")
    void getAllOtts() {
        List<OTT> otts = Arrays.asList(getOttFixture(1L), getOttFixture(2L));
        given(ottRepository.findAll()).willReturn(otts);

        OttListResponse response = ottService.getAll();
        then(response.getOttServices())
            .hasSize(2)
            .extracting("ottId")
            .contains(1L, 2L);
    }

}
