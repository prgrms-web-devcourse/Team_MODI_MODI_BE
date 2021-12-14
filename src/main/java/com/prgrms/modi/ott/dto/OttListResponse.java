package com.prgrms.modi.ott.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class OttListResponse {

    @ApiModelProperty(value = "전체 OTT 리스트")
    private final List<OttNameResponse> ottServices;

    private OttListResponse(List<OttNameResponse> ottServices) {
        this.ottServices = ottServices;
    }

    public static OttListResponse from(List<OttNameResponse> ottServices) {
        return new OttListResponse(ottServices);
    }

    public List<OttNameResponse> getOttServices() {
        return ottServices;
    }

}
