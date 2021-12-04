package com.prgrms.modi.ott.dto;

import com.prgrms.modi.ott.domain.OTT;

public class OttResponse {

    private final Long ottId;

    private final String ottName;

    public OttResponse(OTT ott) {
        this.ottId = ott.getId();
        this.ottName = ott.getName();
    }

    public Long getOttId() {
        return ottId;
    }

    public String getOttName() {
        return ottName;
    }

}
