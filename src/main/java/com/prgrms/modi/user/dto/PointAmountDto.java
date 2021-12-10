package com.prgrms.modi.user.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PointAmountDto {

    private Integer points;

    public PointAmountDto(Integer points) {
        this.points = points;
    }

    public Integer getPoints() {
        return points;
    }

}
