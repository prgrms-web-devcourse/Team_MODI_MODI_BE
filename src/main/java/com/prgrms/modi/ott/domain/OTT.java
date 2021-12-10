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
    private Integer monthlyPrice;

    @Positive
    private Integer maxMemberCapacity;

    @NotBlank
    private String grade;

    protected OTT() {
    }

    private OTT(Builder builder) {
        name = builder.name;
        subscriptionFee = builder.subscriptionFee;
        monthlyPrice = builder.monthlyPrice;
        maxMemberCapacity = builder.maxMemberCapacity;
        grade = builder.grade;
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

    public Integer getmonthlyPrice() {
        return monthlyPrice;
    }

    public Integer getMaxMemberCapacity() {
        return maxMemberCapacity;
    }

    public String getGrade() {
        return grade;
    }

    public static final class Builder {

        private String name;

        private Integer subscriptionFee;

        private Integer monthlyPrice;

        private Integer maxMemberCapacity;

        private String grade;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder subscriptionFee(Integer val) {
            subscriptionFee = val;
            return this;
        }

        public Builder monthlyPrice(Integer val) {
            monthlyPrice = val;
            return this;
        }

        public Builder maxMemberCapacity(Integer val) {
            maxMemberCapacity = val;
            return this;
        }

        public Builder grade(String val) {
            grade = val;
            return this;
        }

        public OTT build() {
            return new OTT(this);
        }

    }

}
