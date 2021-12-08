package com.prgrms.modi.user.domain;

import com.prgrms.modi.common.domain.BaseEntity;
import com.prgrms.modi.party.domain.Party;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Entity
@Table(name = "members")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isLeader;

    @PastOrPresent
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Member() {
    }

    public Member(Party party, User user, boolean isLeader) {
        this.party = party;
        this.user = user;
        this.isLeader = isLeader;
    }

}
