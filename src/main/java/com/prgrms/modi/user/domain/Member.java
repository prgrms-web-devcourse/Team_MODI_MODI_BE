package com.prgrms.modi.user.domain;

import com.prgrms.modi.common.domain.BaseEntity;
import com.prgrms.modi.common.domain.DeletableEntity;
import com.prgrms.modi.party.domain.Party;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.PastOrPresent;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "members")
public class Member extends DeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isLeader;

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

    public Long getId() {
        return id;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("isLeader", isLeader)
            .append("party", party)
            .append("user", user)
            .toString();
    }

}
