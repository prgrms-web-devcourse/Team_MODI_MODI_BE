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
import java.util.Objects;

@Entity
@Table(name = "point_history")
public class PointHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PointDetail pointDetail;

    @PositiveOrZero
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected PointHistory() {
    }

    public PointHistory(PointDetail pointDetail, Integer amount, User user) {
        this.pointDetail = pointDetail;
        this.amount = amount;
        this.user = user;
    }

}
