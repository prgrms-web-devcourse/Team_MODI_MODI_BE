package com.prgrms.modi.point.service;

import static com.prgrms.modi.user.domain.UserTest.getUserFixture;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.prgrms.modi.history.domain.PointHistory;
import com.prgrms.modi.history.repository.PointHistoryRepository;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.dto.PointAmountResponse;
import com.prgrms.modi.user.service.UserService;
import com.prgrms.modi.utils.MockCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PointServiceTest {

    @InjectMocks
    private PointService pointService;

    @Mock
    private UserService userService;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    @Test
    @DisplayName("포인트를 충전할 수 있어야한다.")
    void addPoints() {
        // Given
        User user = getUserFixture(1L);
        given(userService.findUser(anyLong())).willReturn(user);

        PointHistory pointHistory = MockCreator.getPointHistoryFixture(1L);
        given(pointHistoryRepository.save(any(PointHistory.class))).willReturn(pointHistory);

        // When
        PointAmountResponse pointAmountResponse = pointService.addPoints(1L, 10000);

        // Then
        then(pointAmountResponse)
            .hasFieldOrPropertyWithValue("points", pointAmountResponse.getPoints());

        verify(userService).findUser(anyLong());
        verify(user).addPoints(anyInt());
        verify(pointHistoryRepository).save(any(PointHistory.class));
    }

}
