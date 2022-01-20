package com.prgrms.modi.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.prgrms.modi.notification.dto.NotificationsResponse;
import com.prgrms.modi.notification.repository.NotificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("유저의 안 읽은 전체 알림을 조회할 수 있다.")
    @Transactional
    public void getUnreadNotificationsTest() {
        NotificationsResponse notificationsResponse = notificationService.findAllById(1L);
        assertAll(
            () -> assertThat(notificationsResponse.getNotificationResponseList().size()).isEqualTo(3),
            () -> assertThat(notificationsResponse.getUnreadCount()).isEqualTo(3L)
        );
    }

    @Test
    @DisplayName("유저는 알림을 삭제 할 수 있다.")
    @Transactional
    public void readNotificationTest() {
        NotificationsResponse notificationsResponse = notificationService.findAllById(6L);
        assertThat(notificationsResponse.getNotificationResponseList().size()).isEqualTo(1);

        notificationService.readNotification(5L);
        assertAll(
            () -> assertThat(notificationRepository.findById(5L).get().getReadCheck()).isTrue(),
            () -> assertThat(notificationService.findAllById(6L).getNotificationResponseList().size()).isEqualTo(0)
        );
    }

}
