package com.prgrms.modi.ott.domain;

import com.prgrms.modi.common.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "otts")
public class OTT extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @PositiveOrZero
    private Integer subscriptionFee;

    @PositiveOrZero
    private Integer monthlyFee;

    @Positive
    private Integer maxMemberCapacity;

    @NotBlank
    private String grade;

    protected OTT() {
    }

    public OTT(String name, Integer subscriptionFee, Integer monthlyFee, Integer maxMemberCapacity, String grade) {
        this.name = name;
        this.subscriptionFee = subscriptionFee;
        this.monthlyFee = monthlyFee;
        this.maxMemberCapacity = maxMemberCapacity;
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getSubscriptionFee() {
        return subscriptionFee;
    }

    public Integer getMonthlyFee() {
        return monthlyFee;
    }

    public Integer getMaxMemberCapacity() {
        return maxMemberCapacity;
    }

    public String getGrade() {
        return grade;
    }

}
