package com.prgrms.modi.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.prgrms.modi.utils.MockCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NotificationTest {

    @Test
    @DisplayName("알림 정상 생성 테스트")
    void createNotificationTest() {
        Notification notification = new Notification(
            "공유계정 정보가 변경되었습니다.",
            false,
            MockCreator.getMemberFixture(1L),
            MockCreator.getPartyFixture(1L)
        );
        assertAll(
            () -> assertThat(notification).isNotNull(),
            () -> assertThat(notification.getContent()).isEqualTo("공유계정 정보가 변경되었습니다."),
            () -> assertThat(notification.getReadCheck()).isFalse()
        );
    }

}
