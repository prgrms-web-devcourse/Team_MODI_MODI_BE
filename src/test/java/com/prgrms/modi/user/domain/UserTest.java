package com.prgrms.modi.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserTest {

    public static User getUserFixture(Long id) {
        User user = Mockito.mock(User.class);
        given(user.getId()).willReturn(id);
        given(user.getUsername()).willReturn("default");
        given(user.getPoints()).willReturn(0L);
        return user;
    }

    public static User getUserFixture() {
        return getUserFixture(1L);
    }

    @Test
    @DisplayName("유저 정상 생성 테스트")
    void createUserTest() {
        User user = new User(
            "행복한 모디",
            Role.USER, 10L,
            "kakao",
            "1234",
            LocalDate.of(1995, 6, 13)
        );
        assertAll(
            () -> assertThat(user).isNotNull(),
            () -> assertThat(user.getUsername()).isEqualTo("행복한 모디"),
            () -> assertThat(user.getPoints()).isEqualTo(10L)
        );
    }

    @Test
    @DisplayName("유저 생성 실패 테스트")
    void createFailureTest() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new User(
                "",
                Role.USER, 10L,
                "",
                "",
                LocalDate.of(1995, 6, 13)
            ));
    }

}
