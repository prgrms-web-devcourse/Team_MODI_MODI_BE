package com.prgrms.modi.notification.dto;

import com.prgrms.modi.notification.domain.Notification;
import com.prgrms.modi.user.domain.Member;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public class NotificationResponse {

    private Long id;

    private String content;

    private LocalDateTime createdAt;

    private boolean readCheck;

    private Long memberId;

    private Long partyId;

    private String partyLeaderName;

    @QueryProjection
    public NotificationResponse(Long id, String content, LocalDateTime createdAt, boolean readCheck, Long memberId,
        Long partyId, String partyLeaderName) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.readCheck = readCheck;
        this.memberId = memberId;
        this.partyId = partyId;
        this.partyLeaderName = partyLeaderName;
    }

    public static NotificationResponse from(Notification notification) {
        Member member = (Member) notification.getParty().getMembers().stream().filter(Member::isLeader);
        return new NotificationResponse(notification.getId(), notification.getContent(), notification.getCreatedAt(),
            notification.getReadCheck(), notification.getReceiver().getId(), notification.getParty().getId(),
            member.getUser().getUsername()
        );
    }

}
