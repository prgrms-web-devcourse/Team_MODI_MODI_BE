package com.prgrms.modi.notification.dto;

import com.prgrms.modi.notification.domain.Notification;
import com.prgrms.modi.user.domain.Member;
import java.time.LocalDateTime;
import java.util.List;

public class NotificationResponse implements Comparable<NotificationResponse> {

    private Long id;

    private String content;

    private LocalDateTime createdAt;

    private boolean readCheck;

    private Long partyId;

    private String partyLeaderName;

    private String ottName;

    private NotificationResponse(Long id, String content, LocalDateTime createdAt, boolean readCheck,
        Long partyId, String partyLeaderName, String ottName) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.readCheck = readCheck;
        this.partyId = partyId;
        this.partyLeaderName = partyLeaderName;
        this.ottName = ottName;
    }

    public static NotificationResponse from(Notification notification) {
        List<Member> members = notification.getParty().getMembers();
        Member leader = null;
        for (Member member : members) {
            if (member.isLeader()) {
                leader = member;
            }
        }
        return new NotificationResponse(notification.getId(), notification.getContent(), notification.getCreatedAt(),
            notification.getReadCheck(), notification.getParty().getId(), leader.getUser().getUsername(),
            notification.getParty().getOtt().getName()
        );
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isReadCheck() {
        return readCheck;
    }

    public Long getPartyId() {
        return partyId;
    }

    public String getPartyLeaderName() {
        return partyLeaderName;
    }

    public String getOttName() {
        return ottName;
    }

    @Override
    public int compareTo(NotificationResponse o) {
        if (this.getId() > o.getId()) {
            return -1;
        } else if (this.getId() < o.getId()) {
            return 1;
        }
        return 0;
    }

}
