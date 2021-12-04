package com.prgrms.modi.ott.dto;

import java.util.List;

public class AllOttListResponse {

    private final List<OttResponse> ottServices;

    public AllOttListResponse(List<OttResponse> ottServices) {
        this.ottServices = ottServices;
    }

    public List<OttResponse> getOttServices() {
        return ottServices;
    }

}
