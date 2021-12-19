package com.prgrms.modi.party.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.prgrms.modi.history.service.CommissionHistoryService;
import com.prgrms.modi.history.service.PointHistoryService;
import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.repository.OttRepository;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.party.dto.response.PartyListResponse;
import com.prgrms.modi.party.repository.PartyRepository;
import com.prgrms.modi.party.repository.RuleRepository;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.repository.UserRepository;
import com.prgrms.modi.utils.MockCreator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PartyServiceTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @InjectMocks
    private PartyService partyService;

    @Mock
    private PartyRepository partyRepository;

    @Mock
    private OttRepository ottRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private CommissionHistoryService commissionHistoryService;

    @Mock
    private PointHistoryService pointHistoryService;


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
    @DisplayName("유저 파티 가입 테스트")
    void joinParty() {
        User user = MockCreator.getUserFixture(1L, 50000);
        Party party = MockCreator.getPartyFixture(1L);

        given(userRepository.getById(anyLong())).willReturn(user);
        given(partyRepository.getById(anyLong())).willReturn(party);

        partyService.joinParty(user.getId(), party.getId());

        verify(user).deductPoint(anyInt());
    }

}
