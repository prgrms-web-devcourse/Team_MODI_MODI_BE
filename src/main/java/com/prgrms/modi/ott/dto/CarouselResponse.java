package com.prgrms.modi.ott.dto;

public class CarouselResponse {

    private final Long ottId;

    private final String ottName;

    private final Long waitingForMatch;

    private final Integer monthlyFee;

    private CarouselResponse(CarouselInfo carouselInfo) {
        this.ottId = carouselInfo.getOttId();
        this.ottName = carouselInfo.getOttName();
        this.waitingForMatch = carouselInfo.getTotalRecruitingPartyCount();
        this.monthlyFee = carouselInfo.getMonthlyFee();
    }

    public static CarouselResponse from(CarouselInfo carouselInfo) {
        return new CarouselResponse(carouselInfo);
    }

    public Long getOttId() {
        return ottId;
    }

    public String getOttName() {
        return ottName;
    }

    public Long getWaitingForMatch() {
        return waitingForMatch;
    }

    public Integer getMonthlyFee() {
        return monthlyFee;
    }

}
