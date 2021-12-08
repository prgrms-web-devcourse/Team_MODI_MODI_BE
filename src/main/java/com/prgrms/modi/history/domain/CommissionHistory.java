package com.prgrms.modi.history.domain;

import com.prgrms.modi.common.domain.BaseEntity;
import com.prgrms.modi.user.domain.User;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "commission_history")
public class CommissionHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CommissionDetail commissionDetail;

    @PositiveOrZero
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected CommissionHistory() {
    }

    public CommissionHistory(CommissionDetail commissionDetail, Integer amount, User user) {
        this.commissionDetail = commissionDetail;
        this.amount = amount;
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
