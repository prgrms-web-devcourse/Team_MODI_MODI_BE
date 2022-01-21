package com.prgrms.modi.notification.repository;

import com.prgrms.modi.notification.domain.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByMemberIdAndReadCheckFalse(Long memberId);

}
