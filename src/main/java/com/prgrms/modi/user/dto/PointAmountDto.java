package com.prgrms.modi.user.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.annotations.ApiModelProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PointAmountDto {

    @ApiModelProperty(value = "포인트")
    private Integer points;

    public PointAmountDto(Integer points) {
        this.points = points;
    }

    public Integer getPoints() {
        return points;
    }

}
