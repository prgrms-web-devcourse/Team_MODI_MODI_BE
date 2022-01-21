package com.prgrms.modi.notification.domain;

import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.user.domain.Member;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    private String content;

    private boolean readCheck;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id")
    private Party party;

    protected Notification() {
    }

    public Notification(String content, boolean isRead, Member member, Party party) {
        this.content = content;
        this.readCheck = isRead;
        this.createdAt = LocalDateTime.now();
        this.member = member;
        this.party = party;
    }

    public void read() {
        this.readCheck = true;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public boolean getReadCheck() {
        return readCheck;
    }

    public Member getMember() {
        return member;
    }

    public Party getParty() {
        return party;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("content", content)
            .append("readCheck", readCheck)
            .append("createdAt", createdAt)
            .append("member", member)
            .toString();
    }

}
