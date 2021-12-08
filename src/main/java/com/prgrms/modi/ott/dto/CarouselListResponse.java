package com.prgrms.modi.ott.dto;

import java.util.List;

public class CarouselListResponse {

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
