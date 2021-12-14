package com.prgrms.modi.ott.dto;

import io.swagger.annotations.ApiModelProperty;

public class CarouselResponse implements CarouselInfo{

    @ApiModelProperty(value = "OTT id")
    private final Long ottId;

    @ApiModelProperty(value = "OTT 이름")
    private final String ottName;

    @ApiModelProperty(value = "Recruiting 중인 파티 수")
    private final Long waitingForMatch;

    @ApiModelProperty(value = "개월 당 요금")
    private final Integer monthlyPrice;

    private CarouselResponse(CarouselInfo carouselInfo) {
        this.ottId = carouselInfo.getOttId();
        this.ottName = carouselInfo.getOttName();
        this.waitingForMatch = carouselInfo.getWaitingForMatch();
        this.monthlyPrice = carouselInfo.getmonthlyPrice();
    }

    public static CarouselResponse from(CarouselInfo carouselInfo) {
        return new CarouselResponse(carouselInfo);
    }

    @Override
    public Long getOttId() {
        return ottId;
    }

    @Override
    public String getOttName() {
        return ottName;
    }

    @Override
    public Long getWaitingForMatch() {
        return waitingForMatch;
    }

    @Override
    public Integer getmonthlyPrice() {
        return monthlyPrice;
    }

}
