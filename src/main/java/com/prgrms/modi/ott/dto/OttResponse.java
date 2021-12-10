package com.prgrms.modi.ott.dto;

import com.prgrms.modi.ott.domain.OTT;

public class OttResponse {

    private final Long ottId;

    private final String ottName;

    private final Integer subscriptionFee;

    private final Integer monthlyPrice;

    private final Integer maxMemberCapacity;

    private final String grade;

    private OttResponse(OTT ott) {
        this.ottId = ott.getId();
        this.ottName = ott.getName();
        this.subscriptionFee = ott.getSubscriptionFee();
        this.monthlyPrice = ott.getmonthlyPrice();
        this.maxMemberCapacity = ott.getMaxMemberCapacity();
        this.grade = ott.getGrade();
    }

    public static OttResponse from(OTT ott) {
        return new OttResponse(ott);
    }

    public Long getOttId() {
        return ottId;
    }

    public String getOttName() {
        return ottName;
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

}
