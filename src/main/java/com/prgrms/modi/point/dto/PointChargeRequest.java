package com.prgrms.modi.point.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class PointChargeRequest {

    @NotNull
    @PositiveOrZero
    private Integer points;

    protected PointChargeRequest() {
    }

    public PointChargeRequest(Integer points) {
        this.points = points;
    }

    public Integer getPoints() {
        return points;
    }

}
