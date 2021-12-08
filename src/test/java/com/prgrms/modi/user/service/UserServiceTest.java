package com.prgrms.modi.user.service;

import static com.prgrms.modi.user.domain.UserTest.getUserFixture;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.dto.PointAmountDto;
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

    @Test
    @DisplayName("유저를 조회할 수 있다.")
    public void getUserByIdTest() {
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
    public void findUserPointsTest() {
        User user = getUserFixture();
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        PointAmountDto pointAmountDto = userService.getUserPoints(1L);

        then(pointAmountDto)
            .hasFieldOrPropertyWithValue("points", user.getPoints());
    }

}
