package com.prgrms.modi.ott.dto;

import java.util.List;

public class OttListResponse {

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
