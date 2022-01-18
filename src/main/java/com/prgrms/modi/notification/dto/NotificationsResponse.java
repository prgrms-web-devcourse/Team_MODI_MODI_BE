package com.prgrms.modi.notification.dto;

import java.util.List;

public class NotificationsResponse {

    private List<NotificationResponse> notificationResponseList;

    private long unreadCount;

    public List<NotificationResponse> getNotificationResponseList() {
        return notificationResponseList;
    }

    public long getUnreadCount() {
        return unreadCount;
    }

    private NotificationsResponse(
        List<NotificationResponse> notificationResponseList, long unreadCount) {
        this.notificationResponseList = notificationResponseList;
        this.unreadCount = unreadCount;
    }

    public static NotificationsResponse of(List<NotificationResponse> notificationResponses, long unreadCount) {
        return new NotificationsResponse(notificationResponses, unreadCount);
    }


}
