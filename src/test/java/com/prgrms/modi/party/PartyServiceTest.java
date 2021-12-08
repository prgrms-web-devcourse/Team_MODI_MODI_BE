package com.prgrms.modi.party;

import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.service.OttService;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.party.dto.request.CreatePartyRequest;
import com.prgrms.modi.party.dto.request.RuleRequest;
import com.prgrms.modi.party.dto.response.PartyIdResponse;
import com.prgrms.modi.party.dto.response.PartyListResponse;
import com.prgrms.modi.party.repository.PartyRepository;
import com.prgrms.modi.party.service.PartyRuleService;
import com.prgrms.modi.party.service.PartyService;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.service.MemberService;
import com.prgrms.modi.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.prgrms.modi.utils.MockCreator.getPartyFixture;
import static com.prgrms.modi.utils.MockCreator.getUserFixture;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PartyServiceTest {

    @InjectMocks
    private PartyService partyService;

    @Mock
    private PartyRepository partyRepository;

    @Mock
    private OttService ottService;

    @Mock
    private PartyRuleService partyRuleService;

    @Mock
    private MemberService memberService;

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
            .partyMemberCapacity(4)
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
            .partyMemberCapacity(4)
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
            .partyMemberCapacity(4)
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
            .partyMemberCapacity(4)
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

    @Test
    @DisplayName("파티를 생성할 수 있다")
    public void createParty() {
        // Given
        Long ottId = 1L;
        Long userId = 1L;
        RuleRequest ruleRequest1 = new RuleRequest(1L, "1인 1회선");
        RuleRequest ruleRequest2 = new RuleRequest(2L, "양도 금지");
        List<RuleRequest> ruleRequests = List.of(ruleRequest1, ruleRequest2);
        CreatePartyRequest createPartyRequest = new CreatePartyRequest.Builder()
            .ottId(ottId)
            .ottName("넷플릭스")
            .grade("프리미엄")
            .partyMemberCapacity(4)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusMonths(6))
            .mustFilled(true)
            .rules(ruleRequests)
            .sharedId("modi@gmail.com")
            .sharedPassword("modimodi123")
            .build();

        Party party = new Party.Builder()
            .id(1L)
            .partyMemberCapacity(createPartyRequest.getPartyMemberCapacity())
            .currentMemberCapacity(1)
            .totalFee(1000)
            .monthlyReimbursement(2000)
            .remainingReimbursement(8000)
            .startDate(createPartyRequest.getStartDate())
            .endDate(createPartyRequest.getEndDate())
            .mustFilled(createPartyRequest.isMustFilled())
            .ott(getOttFixture(ottId))
            .build();

        when(partyRepository.save(any(Party.class))).thenReturn(party);
        doNothing().when(partyRuleService).savePartyRule(any(Party.class), any(Long.class));
        doNothing().when(memberService).saveLeaderMember(any(Party.class), any(Long.class));

        // When
        PartyIdResponse response = partyService.createParty(createPartyRequest, userId);

        // Then
        verify(partyRepository, times(1)).save(any(Party.class));
        verify(partyRuleService, times(ruleRequests.size())).savePartyRule(any(Party.class), any(Long.class));
        verify(memberService, times(1)).saveLeaderMember(any(Party.class), any(Long.class));
    }

    @Test
    @DisplayName("유저 파티 가입 테스트")
    void joinParty() {
        User user = getUserFixture(1L, 50000L);
        Party party = getPartyFixture(1L);

        given(memberService.findUser(anyLong())).willReturn(user);
        given(partyRepository.findPartyWithOtt(anyLong())).willReturn(Optional.of(party));

        partyService.joinParty(user.getId(), party.getId());

        verify(memberService).findUser(anyLong());
        verify(partyRepository).findPartyWithOtt(anyLong());
        verify(user).deductPoint(anyLong());
        verify(party).increaseCurrentMemberCapacity();
        verify(party).increaseMonthlyReimbursement(anyInt());
        verify(party).increaseRemainingReimbursement(anyInt());
    }

}
