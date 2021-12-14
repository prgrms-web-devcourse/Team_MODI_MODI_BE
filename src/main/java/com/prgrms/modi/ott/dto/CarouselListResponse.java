package com.prgrms.modi.ott.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class CarouselListResponse {

    @ApiModelProperty(value = "캐루셀 전체 리스트")
    private final List<CarouselResponse> waitingOtts;

    private CarouselListResponse(List<CarouselResponse> waitingOtts) {
        this.waitingOtts = waitingOtts;
    }

    public static CarouselListResponse from(List<CarouselResponse> waitingOtts) {
        return new CarouselListResponse(waitingOtts);
    }

    public List<CarouselResponse> getWaitingOtts() {
        return waitingOtts;
    }

}
