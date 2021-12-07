package com.prgrms.modi.party;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.service.OttService;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.party.dto.response.PartyListResponse;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class PartyServiceTest {

    @InjectMocks
    private PartyService partyService;

    @Mock
    private PartyRepository partyRepository;

    @Mock
    private OttService ottService;

    public OTT getOttFixture(Long id) {
        OTT ott = Mockito.mock(OTT.class);
        given(ott.getId()).willReturn(id);
        given(ott.getName()).willReturn("testOttName");
        return ott;
    }

    @Test
    @DisplayName("파티 목록 첫 페이지를 가져올 수 있다")
    public void getFirstPartyList() {
        // Given
        long ottId = 1L;
        int size = 5;

        OTT ott = getOttFixture(ottId);
        Party party1 = new Party.Builder()
            .id(1L)
            .maxMemberCapacity(4)
            .currentMemberCapacity(1)
            .totalFee(1000)
            .monthlyReimbursement(2000)
            .remainingReimbursement(8000)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusMonths(12))
            .mustFilled(true)
            .ott(ott)
            .build();
        Party party2 = new Party.Builder()
            .id(2L)
            .maxMemberCapacity(4)
            .currentMemberCapacity(1)
            .totalFee(1000)
            .monthlyReimbursement(2000)
            .remainingReimbursement(8000)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusMonths(12))
            .mustFilled(true)
            .ott(ott)
            .build();
        List<Party> partyList = List.of(party2, party1);
        LocalDate minDate = LocalDate.of(0, 1, 1);
        PageRequest page = PageRequest.of(0, size);

        when(ottService.findOtt(1L)).thenReturn(ott);
        when(partyRepository.findAllRecruitingParty(ott, PartyStatus.RECRUITING, minDate, Long.MAX_VALUE, page))
            .thenReturn(partyList);

        // When
        PartyListResponse response = partyService.getPartyList(ottId, size);

        // Then
        assertThat(response.getPartyList().size(), equalTo(2));
    }

    @Test
    @DisplayName("파티 목록을 커서 기반 페이지로 가져올 수 있다")
    public void getCursorPartyList() {
        // Given
        long ottId = 1L;
        long lastPartyId = 2L;
        int size = 5;

        OTT ott = getOttFixture(ottId);
        Party party1 = new Party.Builder()
            .id(1L)
            .maxMemberCapacity(4)
            .currentMemberCapacity(1)
            .totalFee(1000)
            .monthlyReimbursement(2000)
            .remainingReimbursement(8000)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusMonths(12))
            .mustFilled(true)
            .ott(ott)
            .build();
        Party party2 = new Party.Builder()
            .id(2L)
            .maxMemberCapacity(4)
            .currentMemberCapacity(1)
            .totalFee(1000)
            .monthlyReimbursement(2000)
            .remainingReimbursement(8000)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusMonths(12))
            .mustFilled(true)
            .ott(ott)
            .build();
        List<Party> partyList = List.of(party1);
        LocalDate minDate = LocalDate.of(0, 1, 1);
        PageRequest page = PageRequest.of(0, size);

        when(ottService.findOtt(1L)).thenReturn(ott);
        doReturn(partyList)
            .when(partyRepository).findAllRecruitingParty(ott, PartyStatus.RECRUITING, minDate, Long.MAX_VALUE, page);

        // When
        PartyListResponse response = partyService.getPartyList(ottId, size);

        // Then
        assertThat(response.getPartyList().size(), equalTo(1));
    }

}
