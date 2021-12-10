package com.prgrms.modi.user.service;

import static com.prgrms.modi.user.domain.UserTest.getUserFixture;
import static com.prgrms.modi.utils.MockCreator.getPartyFixture;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.dto.response.PartyDetailResponse;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.party.repository.PartyRepository;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.dto.PointAmountDto;
import com.prgrms.modi.user.dto.UserPartyListResponse;
import com.prgrms.modi.user.dto.UserResponse;
import com.prgrms.modi.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PartyRepository partyRepository;

    @Test
    @DisplayName("유저를 조회할 수 있다.")
    public void getUserDetailTest() {
        User user = getUserFixture();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        UserResponse userResponse = userService.getUserDetail(1L);

        then(userResponse)
            .hasFieldOrPropertyWithValue("userId", user.getId())
            .hasFieldOrPropertyWithValue("username", user.getUsername())
            .hasFieldOrPropertyWithValue("points", user.getPoints());
    }

    @Test
    @DisplayName("유저 포인트를 조회할 수 있다.")
    public void getUserPointsTest() {
        User user = getUserFixture();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        PointAmountDto pointAmountDto = userService.getUserPoints(1L);

        then(pointAmountDto)
            .hasFieldOrPropertyWithValue("points", user.getPoints());
    }

    @Test
    @DisplayName("유저가 참여한 파티 상세 정보를 조회할 수 있다.")
    public void getUserPartyTest() {
        User user = getUserFixture();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        Party party = getPartyFixture(1L);
        given(partyRepository.findById(party.getId())).willReturn(Optional.of(party));

        PartyDetailResponse userPartyDetail = userService.getUserPartyDetail(party.getId());

        then(userPartyDetail)
            .hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("유저가 참여한 파티 목록을 조회할 수 있다. -  RECRUITING")
    public void getUserRecruitingPartiesTest() {
        User user = getUserFixture();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        UserPartyListResponse userRecruitingPartyList = userService
            .getUserPartyList(user.getId(), PartyStatus.RECRUITING, 5, null);

        then(userRecruitingPartyList)
            .hasFieldOrProperty("parties");
    }

    @Test
    @DisplayName("유저가 참여한 파티 목록을 조회할 수 있다. -  ONGOING")
    public void getUserOnGoingPartiesTest() {
        User user = getUserFixture();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        UserPartyListResponse userRecruitingPartyList = userService
            .getUserPartyList(user.getId(), PartyStatus.ONGOING, 5, null);

        then(userRecruitingPartyList)
            .hasFieldOrProperty("parties");
    }

    @Test
    @DisplayName("유저가 참여한 파티 목록을 조회할 수 있다. -  FINISHED")
    public void getUserFinishedPartiesTest() {
        User user = getUserFixture();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        UserPartyListResponse userRecruitingPartyList = userService
            .getUserPartyList(user.getId(), PartyStatus.FINISHED, 5, null);

        then(userRecruitingPartyList)
            .hasFieldOrProperty("parties");
    }

}
