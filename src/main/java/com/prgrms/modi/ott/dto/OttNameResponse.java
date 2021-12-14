package com.prgrms.modi.ott.dto;

import com.prgrms.modi.ott.domain.OTT;
import io.swagger.annotations.ApiModelProperty;

public class OttNameResponse {

    @ApiModelProperty(value = "OTT id")
    private final Long ottId;

    @ApiModelProperty(value = "OTT 이름")
    private final String ottName;

    @ApiModelProperty(value = "OTT 영어 이름")
    private final String ottNameEn;

    private OttNameResponse(OTT ott) {
        this.ottId = ott.getId();
        this.ottName = ott.getName();
        this.ottNameEn = ott.getEnglishName();
    }

    public static OttNameResponse from(OTT ott) {
        return new OttNameResponse(ott);
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

}
