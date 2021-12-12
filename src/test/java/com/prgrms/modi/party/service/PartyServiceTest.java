package com.prgrms.modi.party.service;

import static com.prgrms.modi.utils.MockCreator.getPartyFixture;
import static com.prgrms.modi.utils.MockCreator.getUserFixture;
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

import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.repository.OttRepository;
import com.prgrms.modi.ott.service.OttService;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.party.dto.request.CreatePartyRequest;
import com.prgrms.modi.party.dto.request.RuleRequest;
import com.prgrms.modi.party.dto.response.PartyIdResponse;
import com.prgrms.modi.party.dto.response.PartyListResponse;
import com.prgrms.modi.party.repository.PartyRepository;
import com.prgrms.modi.party.repository.RuleRepository;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.repository.UserRepository;
import com.prgrms.modi.user.service.MemberService;
import com.prgrms.modi.utils.MockCreator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PartyServiceTest {

    @InjectMocks
    private PartyService partyService;

    @Mock
    private PartyRepository partyRepository;

    @Mock
    private OttRepository ottRepository;

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OttService ottService;

    @Mock
    private MemberService memberService;

    @Test
    @DisplayName("파티 목록 커서 기반 페이지로 가져올 수 있다")
    public void getFirstPartyList() {
        // Given
        long ottId = 1L;
        int size = 5;
        int partySize = 5;
        int period = 6;

        OTT ott = MockCreator.getOttFixture(ottId);
        List<Party> parties = new ArrayList<>();
        for (long i = 0; i < partySize; i++) {
            parties.add(MockCreator.getPartyFixture(i));
        }

        doReturn(ott)
            .when(ottRepository)
            .getById(any(Long.class));

        doReturn(parties)
            .when(partyRepository)
            .findPartyPage(
                any(OTT.class),
                any(PartyStatus.class),
                any(LocalDate.class),
                any(Long.class),
                any(Pageable.class)
            );

        // When
        PartyListResponse response = partyService.getPartyList(ottId, size);

        // Then
        verify(ottRepository, times(1))
            .getById(anyLong());
        verify(partyRepository, times(1))
            .findPartyPage(
                any(OTT.class),
                any(PartyStatus.class),
                any(LocalDate.class),
                any(Long.class),
                any(Pageable.class)
            );

        assertThat(response.getPartyList().size(), equalTo(partySize));
        assertThat(response.getPartyList().get(0).getPeriod(), equalTo(period));
    }

    @Test
    @DisplayName("파티를 생성할 수 있다")
    public void createParty() {
        // Given
        Long ottId = 1L;
        Long userId = 1L;
        OTT ott = MockCreator.getOttFixture(ottId);

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

        Party party = MockCreator.getPartyFixture(1L);

        when(partyRepository.save(any(Party.class))).thenReturn(party);
        doNothing().when(memberService).saveLeaderMember(any(Party.class), any(Long.class));
        when(ottRepository.getById(anyLong())).thenReturn(ott);

        // When
        PartyIdResponse response = partyService.createParty(createPartyRequest, userId);

        // Then

    }

    @Test
    @DisplayName("유저 파티 가입 테스트")
    void joinParty() {
        User user = getUserFixture(1L, 50000);
        Party party = getPartyFixture(1L);

        given(memberService.findUser(anyLong())).willReturn(user);
        given(partyRepository.findPartyWithOtt(anyLong())).willReturn(Optional.of(party));

        partyService.joinParty(user.getId(), party.getId());

        verify(memberService).findUser(anyLong());
        verify(partyRepository).findPartyWithOtt(anyLong());
        verify(user).deductPoint(anyInt());
        verify(party).increaseCurrentMemberCapacity();
        verify(party).increaseMonthlyReimbursement(anyInt());
        verify(party).increaseRemainingReimbursement(anyInt());
    }

}
