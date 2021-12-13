package com.prgrms.modi.ott.dto;

import com.prgrms.modi.ott.domain.OTT;

public class OttResponse {

    private final Long ottId;

    private final String ottName;

    private final String ottNameEn;

    private final Integer subscriptionFee;

    private final Integer monthlyPrice;

    private final Integer maxMemberCapacity;

    private final String grade;

    private OttResponse(OTT ott) {
        this.ottId = ott.getId();
        this.ottName = ott.getName();
        this.ottNameEn = ott.getEnglishName();
        this.subscriptionFee = ott.getSubscriptionFee();
        this.monthlyPrice = ott.getMonthlyPrice();
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

    public String getOttNameEn() {
        return ottNameEn;
    }

    public Integer getSubscriptionFee() {
        return subscriptionFee;
    }

    public Integer getMonthlyPrice() {
        return monthlyPrice;
    }

    public Integer getMaxMemberCapacity() {
        return maxMemberCapacity;
    }

    public String getGrade() {
        return grade;
    }

}
