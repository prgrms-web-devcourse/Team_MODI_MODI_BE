package com.prgrms.modi.ott.dto;

import com.prgrms.modi.ott.domain.OTT;
import io.swagger.annotations.ApiModelProperty;

public class OttResponse {

    @ApiModelProperty(value = "OTT id")
    private final Long ottId;

    @ApiModelProperty(value = "OTT 이름")
    private final String ottName;

    @ApiModelProperty(value = "OTT 영어 이름")
    private final String ottNameEn;

    @ApiModelProperty(value = "OTT 구독료")
    private final Integer subscriptionFee;

    @ApiModelProperty(value = "개월 당 요금")
    private final Integer monthlyPrice;

    @ApiModelProperty(value = "파티 정원")
    private final Integer maxMemberCapacity;

    @ApiModelProperty(value = "OTT 서비스 등급")
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
