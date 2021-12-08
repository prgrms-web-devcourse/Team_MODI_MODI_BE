package com.prgrms.modi.ott.dto;

public class CarouselResponse implements CarouselInfo{

    private final Long ottId;

    private final String ottName;

    private final Long waitingForMatch;

    private final Integer monthlyFee;

    private CarouselResponse(CarouselInfo carouselInfo) {
        this.ottId = carouselInfo.getOttId();
        this.ottName = carouselInfo.getOttName();
        this.waitingForMatch = carouselInfo.getWaitingForMatch();
        this.monthlyFee = carouselInfo.getMonthlyFee();
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
    public Integer getMonthlyFee() {
        return monthlyFee;
    }

}
