package com.prgrms.modi.notification.repository;

import com.prgrms.modi.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
