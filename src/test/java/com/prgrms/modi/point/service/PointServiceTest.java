package com.prgrms.modi.point.service;

import com.prgrms.modi.history.domain.PointDetail;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.dto.PointAmountDto;
import com.prgrms.modi.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static com.prgrms.modi.user.domain.UserTest.getUserFixture;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PointServiceTest {

    @InjectMocks
    private PointService pointService;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("포인트를 충전할 수 있어야한다.")
    void addPoints() {
        User user = getUserFixture(1L);
        given(userService.findUser(anyLong())).willReturn(user);

        PointAmountDto pointAmountDto = pointService.addPoints(1L, 10000);

        then(pointAmountDto)
            .hasFieldOrPropertyWithValue("points", pointAmountDto.getPoints());

        verify(userService).findUser(anyLong());
        verify(user).addPoints(anyInt());
        verify(userService).savePointHistory(PointDetail.ADD, 10000, user);

    }

}
